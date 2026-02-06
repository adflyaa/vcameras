package com.hack.opensdk

import android.app.Application
import android.content.Context

/**
 * Base Application class for VirtualApp framework
 */
open class HackApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: HackApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Initialize virtual environment
        HackApi.initialize(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
