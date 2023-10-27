package com.opensource.svgaplayer

import android.app.Application
import com.opensource.svgaplayer.utils.PathUtils
import com.opensource.svgaplayer.utils.log.SVGALogger.setLogEnabled

object SVGA {
    @JvmStatic
    internal lateinit var app: Application

    fun init(app: Application): SVGA {
        this.app = app
        PathUtils.init()
        return this
    }

    fun enableLog(enable: Boolean): SVGA {
        setLogEnabled(enable)
        return this
    }
}