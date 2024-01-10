package com.opensource.svgaplayer

import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.getLifecycle
import com.opensource.svgaplayer.utils.inflate
import com.opensource.svgaplayer.utils.log.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSource

class SVGADecoder(
    private val array: ByteArray,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Decoder {
    private val TAG = javaClass.simpleName

    override suspend fun decode(): DecodeResult {
        LogUtils.error("SVGADecoder", "SVGA解析成功")

        val hashCode = array.contentHashCode().toString()
        LogUtils.error(TAG, "byteArray的hashCode：${hashCode}")

//        val cacheEntity = MovieEntityFactory.get(hashCode)
//        val movieEntity = cacheEntity?.let {
//            it
//        } ?: run {
//            MovieEntity.ADAPTER.decode(array)
//        }

        val movieEntity = MovieEntity.ADAPTER.decode(array)
        val entity = SVGAVideoEntity(hashCode, imageLoader, movieEntity)
        val drawable = SVGADrawable(entity, options, imageLoader)
        withContext(Dispatchers.Main) {
            val lifecycle = options.context.getLifecycle() ?: SVGALifecycle
            lifecycle.addObserver(drawable)
        }
        return DecodeResult(drawable, false)
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder? {
            LogUtils.error("SVGADecoder", "SVGA解码器解析文件类型：" + result.mimeType.toString())
            val source = result.source.source()
            return if (isSVGA(source)) {
                val byteArray = source.readByteArray()
                val inflate = byteArray.inflate()
                SVGADecoder(inflate, options, imageLoader)
            } else {
                null
            }
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()

        private fun isSVGA(source: BufferedSource): Boolean {
            return source.buffer[0].toInt() == 120 && source.buffer[1].toInt() == -100
        }
    }

    companion object {
        const val REPEAT_COUNT_KEY = "svga#repeatCount"
        const val ANIMATION_START_CALLBACK_KEY = "svga#animation_start_callback"
        const val ANIMATION_END_CALLBACK_KEY = "svga#animation_end_callback"
        const val ANIMATION_FRAME_CALLBACK_KEY = "svga#animation_frame_callback"
    }
}