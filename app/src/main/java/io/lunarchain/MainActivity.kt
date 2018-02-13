package io.lunarchain

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wallet -> {
                currentFragment = WalletFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.content, currentFragment)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                currentFragment = MineFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.content, currentFragment)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_transfer -> {
                currentFragment = TransferFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.content, currentFragment)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentFragment = WalletFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, currentFragment)
        ft.commit()


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
