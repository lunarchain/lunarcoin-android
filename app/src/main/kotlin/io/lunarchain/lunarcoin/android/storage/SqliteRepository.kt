package io.lunarchain.lunarcoin.android.storage

import io.lunarchain.lunarcoin.core.AccountWithKey
import io.lunarchain.lunarcoin.core.Block
import io.lunarchain.lunarcoin.core.Transaction
import io.lunarchain.lunarcoin.storage.BlockInfo
import io.lunarchain.lunarcoin.storage.Repository
import java.math.BigInteger

class SqliteRepository: Repository {
    override fun getBalance(address: ByteArray): BigInteger {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun increaseNonce(address: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addBalance(address: ByteArray, amount: BigInteger) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAccount(account: AccountWithKey, password: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccount(index: Int, password: String): AccountWithKey? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun accountNumber(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlockInfo(hash: ByteArray): BlockInfo? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlockInfos(height: Long): List<BlockInfo>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlock(hash: ByteArray): Block? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveBlock(block: Block) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestBlock(): Block? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBestBlock(block: Block) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putTransaction(trx: Transaction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeAccountStateRoot(stateRoot: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccountStateRoot(): ByteArray? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlockInfo(height: Long, blockInfo: BlockInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
