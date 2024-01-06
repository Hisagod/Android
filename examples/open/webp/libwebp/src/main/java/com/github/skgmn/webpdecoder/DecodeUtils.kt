package com.github.skgmn.webpdecoder

import okio.BufferedSource
import okio.ByteString.Companion.encodeUtf8
import kotlin.experimental.and

object DecodeUtils {
    // https://developers.google.com/speed/webp/docs/riff_container
    private val WEBP_HEADER_RIFF = "RIFF".encodeUtf8()
    private val WEBP_HEADER_WEBP = "WEBP".encodeUtf8()
    private val WEBP_HEADER_VPX8 = "VP8X".encodeUtf8()

    /**
     * Return 'true' if the [source] contains a WebP image. The [source] is not consumed.
     */
    private fun isWebP(source: BufferedSource): Boolean {
        return source.rangeEquals(0, WEBP_HEADER_RIFF) &&
                source.rangeEquals(8, WEBP_HEADER_WEBP)
    }

    /**
     * Return 'true' if the [source] contains an animated WebP image. The [source] is not consumed.
     */
    fun isAnimatedWebP(source: BufferedSource): Boolean {
        return isWebP(source) &&
                source.rangeEquals(12, WEBP_HEADER_VPX8) &&
                source.request(17) &&
                (source.buffer[16] and 0b00000010) > 0
    }
}