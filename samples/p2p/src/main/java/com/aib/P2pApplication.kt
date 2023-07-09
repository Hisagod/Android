package com.aib

import android.app.Application
import android.content.Context
import android.os.Handler
import com.aib.p2p.R
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class P2pApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initArouter()
        initUtils()
        initMMKV()
    }

    private fun initArouter() {
        if (AppUtils.isAppDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    private fun initUtils() {
        Utils.init(this)
    }

    private fun initMMKV() {
        val mmkvPath = MMKV.initialize(this)
        LogUtils.e("MMKV路径：$mmkvPath")
    }
}
