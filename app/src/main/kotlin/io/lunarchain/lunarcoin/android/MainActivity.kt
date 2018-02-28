package io.lunarchain.lunarcoin.android

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.lunarchain.R
import io.lunarchain.lunarcoin.android.storage.SqliteRepository
import io.lunarchain.lunarcoin.config.BlockChainConfig
import io.lunarchain.lunarcoin.core.BlockChain
import io.lunarchain.lunarcoin.core.BlockChainManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(this)
        navigation.selectedItemId = R.id.navigation_wallet
        initBlockChain()
    }

    override fun onDestroy() {
        super.onDestroy()
        BlockChainManager.INSTANCE.stop()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        replaceFragment(item.itemId)
        return true
    }

    private fun initBlockChain() {
        //val dir = content.context.filesDir
        assets.open("application.conf").use {
            val config = BlockChainConfig(it)
            val blockChain = BlockChain(config, SqliteRepository(LunarCoinApp.context))
            val manager = BlockChainManager(blockChain)
            manager.startPeerDiscovery()
        }
    }

    private fun replaceFragment(id: Int) {
        var currentFragment = when (id) {
            R.id.navigation_wallet -> WalletFragment()
            R.id.navigation_mine -> MineFragment()
            R.id.navigation_transfer -> TransferFragment()
            else -> WalletFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.content, currentFragment).commit()
    }

}
