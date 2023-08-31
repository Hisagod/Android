package com.opensource.svgaplayer.utils

import coil.decode.DecodeUtils
import okio.BufferedSource

fun DecodeUtils.isSVGA(source: BufferedSource): Boolean {
    return source.buffer[0].toInt() == 120 && source.buffer[1].toInt() == -100
}