package com.example.webp.lib

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
        val bytes = result.source.source().readByteArray()
        val key = bytes.contentHashCode().toString()
        if (key.isNullOrEmpty()) {
            throw Exception("缓存Key获取失败")
        }
        val byteBuffer = ByteBuffer.allocateDirect(bytes.size).put(bytes)
        val decoder = LibWebPAnimatedDecoder.create(key, byteBuffer, options.premultipliedAlpha)
        val image = mutableListOf<LibWebPAnimatedDecoder.DecodeFrameResult>()
        for (index in 0 until decoder.frameCount) {
            val result = decoder.decodeNextFrame(index, key, imageLoader)
            result?.let {
                image.add(it)
            }
        }
        val width = decoder.width
        val height = decoder.height
        val loopCount = decoder.loopCount
        val frameCount = decoder.frameCount
        return DecodeResult(
            AnimatedWebPDrawable(width, height, image, loopCount, frameCount),
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
            Logger.e(TAG, "创建低版本Webp解码器")
            //如果不是webp动图就返回null
            if (!DecodeUtils.isAnimatedWebP(result.source.source())) {
                return null
            }
            return AnimatedWebPDecoder(result, options, imageLoader)
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }
}