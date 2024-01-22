package com.example.webp.lib

import androidx.core.graphics.scale
import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import java.nio.ByteBuffer

class AnimatedWebPDecoder(
    private val result: SourceResult,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Decoder {
    private val TAG = javaClass.simpleName

    override suspend fun decode(): DecodeResult? {
//        Logger.e(TAG, "宽" + options.size.width.toString())
//        Logger.e(TAG, "高" + options.size.height.toString())

        val bytes = result.source.source().readByteArray()
        val key = bytes.contentHashCode().toString()
        if (key.isNullOrEmpty()) {
            throw Exception("缓存Key获取失败")
        }
        val byteBuffer = ByteBuffer.allocateDirect(bytes.size).put(bytes)
        val decoder = LibWebPAnimatedDecoder.create(key, byteBuffer, options.premultipliedAlpha)
        val image = mutableListOf<LibWebPAnimatedDecoder.DecodeFrameResult>()

        val scaleWidth = (decoder.width * 0.5f).toInt()
        val scaleHeight = (decoder.height * 0.5f).toInt()

        for (index in 0 until decoder.frameCount) {
            val result = decoder.decodeNextFrame(index, key, scaleWidth, scaleHeight, imageLoader)
            result?.let {
                image.add(it)
            }
        }
        val loopCount = decoder.loopCount
        val frameCount = decoder.frameCount
        return DecodeResult(
            AnimatedWebPDrawable(scaleWidth, scaleHeight, image, loopCount, frameCount),
            false
        )
    }

    class Factory : Decoder.Factory {

        private val TAG = javaClass.simpleName

        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder? {
            val headerBytes =
                result.source.source().peek().readByteArray(WebPSupportStatus.HEADER_SIZE)
            val isWebpAnim = (WebPSupportStatus.isWebpHeader(
                headerBytes,
                0,
                headerBytes.size
            ) && WebPSupportStatus.isAnimatedWebpHeader(headerBytes, 0))

            if (isWebpAnim) {
                Logger.e(TAG, "创建低版本Webp解码器")
                return AnimatedWebPDecoder(result, options, imageLoader)
            }

            return null
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }
}