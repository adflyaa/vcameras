package virtual.camera.app.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hack.opensdk.HackApplication
import virtual.camera.camera.MultiPreferences
import virtual.camera.app.core.VirtualAppCore

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 21:21
 */
class App : HackApplication() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var mContext: Context

        @JvmStatic
        fun getContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize MultiPreferences for camera settings
        MultiPreferences.initialize(this)
        
        // Initialize VirtualApp Core for app virtualization and camera hooking
        VirtualAppCore.initialize(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mContext = base!!
    }
}