package io.lunarchain.lunarcoin.android.storage

import android.content.Context
import io.lunarchain.lunarcoin.core.AccountState
import io.lunarchain.lunarcoin.core.AccountWithKey
import io.lunarchain.lunarcoin.core.Block
import io.lunarchain.lunarcoin.core.Transaction
import io.lunarchain.lunarcoin.serialization.AccountSerialize
import io.lunarchain.lunarcoin.serialization.BlockInfosSerialize
import io.lunarchain.lunarcoin.serialization.BlockSerialize
import io.lunarchain.lunarcoin.serialization.TransactionSerialize
import io.lunarchain.lunarcoin.storage.BlockInfo
import io.lunarchain.lunarcoin.storage.ObjectStore
import io.lunarchain.lunarcoin.storage.Repository
import io.lunarchain.lunarcoin.trie.PatriciaTrie
import io.lunarchain.lunarcoin.util.CodecUtil
import lunar.vm.DataWord
import java.math.BigInteger

class SqliteRepository(val context: Context) : Repository {

    override fun saveCode(addr: ByteArray, code: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCodeHash(addr: ByteArray): ByteArray? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBalance(address: ByteArray): BigInteger {
        return getAccountState(address)?.balance ?: BigInteger.ZERO
    }

    override fun increaseNonce(address: ByteArray) {
        val accountState = getOrCreateAccountState(address)
        accountStateDs.update(
            address,
            CodecUtil.encodeAccountState(accountState.increaseNonce())
        )
    }

    override fun addBalance(address: ByteArray, amount: BigInteger) {
        val accountState = getOrCreateAccountState(address)
        accountStateDs.update(
            address,
            CodecUtil.encodeAccountState(accountState.increaseBalance(amount))
        )
    }

    override fun saveAccount(account: AccountWithKey, password: String): Int {
        val ds = getAccountStore(password)
        val index = ds?.keys()?.size ?: 0
        val key = index.toString(10).toByteArray()
        ds?.put(key, account)
        return index
    }

    override fun getAccount(index: Int, password: String): AccountWithKey? {
        val ds = getAccountStore(password)
        val key = index.toString(10).toByteArray()
        val account = ds?.get(key)
        account?.index = index
        return account
    }

    override fun accountNumber(): Int {
        val ds = getAccountStore("")
        val keys = ds?.keys()
        return keys?.size ?: 0
    }

    override fun getBlockInfo(hash: ByteArray): BlockInfo? {
        val block = getBlock(hash)
        if (block != null) {
            val blockInfos = getBlockInfos(block.height)
            return blockInfos?.first { it.hash.contentEquals(block.hash) }
        } else {
            return null
        }
    }

    override fun getBlockInfos(height: Long): List<BlockInfo>? {
        return getBlockIndexStore()?.get(CodecUtil.longToByteArray(height))
    }

    override fun getBlock(hash: ByteArray): Block? {
        return getBlockStore()?.get(hash)
    }

    override fun saveBlock(block: Block) {
        getBlockStore()?.put(block.hash, block)
    }

    override fun getBestBlock(): Block? {
        return getBestBlockStore()?.get(BEST_BLOCK_KEY)
    }

    override fun updateBestBlock(block: Block) {
        getBestBlockStore()?.put(BEST_BLOCK_KEY, block)
    }

    override fun putTransaction(trx: Transaction) {
        getTransactionStore()?.put(trx.hash(), trx)
    }

    override fun close() {
        SqliteDbHelper(context).close()
    }

    override fun changeAccountStateRoot(stateRoot: ByteArray) {
        accountStateDs.changeRoot(stateRoot)
    }

    override fun getAccountStateRoot(): ByteArray? {
        return accountStateDs.rootHash
    }

    override fun updateBlockInfo(height: Long, blockInfo: BlockInfo) {
        val blockIndexStore = getBlockIndexStore()
        val k = CodecUtil.longToByteArray(height)
        val blockInfoList = blockIndexStore?.get(k)
        if (blockInfoList != null) {
            val filtered = blockInfoList.dropWhile { it.hash.contentEquals(blockInfo.hash) }
            val converted = filtered.map { BlockInfo(it.hash, false, it.totalDifficulty) }
            blockIndexStore.put(CodecUtil.longToByteArray(height), converted.plus(blockInfo))
        } else {
            blockIndexStore?.put(CodecUtil.longToByteArray(height), listOf(blockInfo))
        }
    }

    override fun getAccountState(address: ByteArray) = accountStateDs.get(address).let { CodecUtil.decodeAccountState(it) }

    override fun isExist(address: ByteArray): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStorageValue(addr: ByteArray, key: DataWord): DataWord {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCode(addr: ByteArray): ByteArray? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlockHashByNumber(blockNumber: Long, branchBlockHash: ByteArray): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addStorageRow(addr: ByteArray, key: DataWord, value: DataWord) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val accountStateDs = PatriciaTrie(SqliteDataSource(SqliteDbHelper.TABLE_NAME_ACCOUNT_STATE, SqliteDbHelper(context)))

    private val accountDs = SqliteDataSource(SqliteDbHelper.TABLE_NAME_ACCOUNT, SqliteDbHelper(context))

    private val blockDs = SqliteDataSource(SqliteDbHelper.TABLE_NAME_BLOCK, SqliteDbHelper(context))

    private val blockIndexDs = SqliteDataSource(SqliteDbHelper.TABLE_NAME_BLOCK_INDEX, SqliteDbHelper(context))

    private val transactionDs = SqliteDataSource(SqliteDbHelper.TABLE_NAME_BLOCK_TRANSACTION, SqliteDbHelper(context))

    private val bestBlockDs = SqliteDataSource(SqliteDbHelper.TABLE_NAME_BEST_BLOCK, SqliteDbHelper(context))

    private val BEST_BLOCK_KEY = "0".toByteArray()

    private fun createAccountState(address: ByteArray): AccountState {
        val state = AccountState(BigInteger.ZERO, BigInteger.ZERO)
        accountStateDs.update(address, CodecUtil.encodeAccountState(state))
        return state
    }

    override fun getOrCreateAccountState(address: ByteArray): AccountState {
        var ret = getAccountState(address)
        if (ret == null) {
            ret = createAccountState(address)
        }
        return ret
    }

    /**
     * Account的存储类组装。
     */
    private fun getAccountStore(password: String): ObjectStore<AccountWithKey>? {
        return ObjectStore(accountDs, AccountSerialize(password))
    }

    /**
     * Block的存储类组装。
     */
    private fun getBlockStore(): ObjectStore<Block>? {
        return ObjectStore(blockDs, BlockSerialize())
    }

    /**
     * Transaction的存储类组装。
     */
    private fun getTransactionStore(): ObjectStore<Transaction>? {
        return ObjectStore(transactionDs, TransactionSerialize())
    }

    /**
     * Block Index的存储类组装。
     */
    private fun getBlockIndexStore(): ObjectStore<List<BlockInfo>>? {
        return ObjectStore(blockIndexDs, BlockInfosSerialize())
    }

    /**
     * BestBlock的存储类组装。
     */
    private fun getBestBlockStore(): ObjectStore<Block>? {
        return ObjectStore(bestBlockDs, BlockSerialize())
    }
}
