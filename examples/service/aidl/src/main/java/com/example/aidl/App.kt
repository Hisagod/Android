package com.example.aidl

import android.app.Application

class App : Application() {


    companion object {
        @JvmStatic
        lateinit var instance: Application
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}