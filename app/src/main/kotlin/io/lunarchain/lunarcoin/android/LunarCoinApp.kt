package io.lunarchain.lunarcoin.android

import android.app.Application
import android.content.Context

class LunarCoinApp: Application() {

    companion object {
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}
