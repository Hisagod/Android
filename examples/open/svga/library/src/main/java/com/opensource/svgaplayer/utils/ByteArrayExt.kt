package com.opensource.svgaplayer.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.Inflater

fun ByteArray.convertSVGA(): ByteArray {
    val inflater = Inflater()
    inflater.setInput(this, 0, this.size)
    val inflatedBytes = ByteArray(2048)
    ByteArrayOutputStream().use { inflatedOutputStream ->
        while (true) {
            val count = inflater.inflate(inflatedBytes, 0, 2048)
            if (count <= 0) {
                break
            } else {
                inflatedOutputStream.write(inflatedBytes, 0, count)
            }
        }
        inflater.end()
        return inflatedOutputStream.toByteArray()
    }
}