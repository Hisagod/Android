package com.opensource.svgaplayer.utils

object ByteArrayUtil {
    fun getHashCode(byteArray: ByteArray): Int {
        return byteArray.contentHashCode()
    }
}