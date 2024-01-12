package com.opensource.svgaplayer

import com.opensource.svgaplayer.utils.log.SVGALogger.setLogEnabled

object SVGA {
    fun enableLog(enable: Boolean): SVGA {
        setLogEnabled(enable)
        return this
    }
}