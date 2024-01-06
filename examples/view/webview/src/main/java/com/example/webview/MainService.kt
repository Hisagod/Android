package com.example.webview

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.MessengerUtils

class MainService : Service() {

    override fun onCreate() {
        super.onCreate()
        MessengerUtils.register()

        MessengerUtils.subscribe("fromMain") {
            val name = it.getString("name")
            LogUtils.e(name)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        MessengerUtils.unregister()
    }
}