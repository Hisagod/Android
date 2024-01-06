package com.example.webp.lib

import android.util.Log

object Logger {
    @Volatile
    private var debug = false

    fun debug(value: Boolean) {
        debug = value
    }

    fun e(tag: String, content: String) {
        if (debug) {
            return
        }

        Log.e(tag, content)
    }
}