package com.opensource.svgaplayer

import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.getLifecycle
import com.opensource.svgaplayer.utils.convertSVGA
import com.opensource.svgaplayer.utils.log.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okio.BufferedSource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SVGADecoder(
    private val array: ByteArray,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Decoder {
    private val TAG = javaClass.simpleName

    override suspend fun decode(): DecodeResult {
        val hashCode = array.contentHashCode().toString()
        LogUtils.error(TAG, "byteArray的hashCode：${hashCode}")

        val movieEntity = MovieEntity.ADAPTER.decode(array)
        val entity = suspendCancellableCoroutine { cb ->
            try {
                SVGAVideoEntity(hashCode, imageLoader, movieEntity) {
                    cb.resume(it)
                }
            } catch (e: Exception) {
                cb.resumeWithException(e)
            }
        }
        val drawable = SVGADrawable(entity, options, imageLoader)
//        withContext(Dispatchers.Main) {
//            val lifecycle = options.context.getLifecycle() ?: SVGALifecycle
//            lifecycle.addObserver(drawable)
//        }
        return DecodeResult(drawable, false)
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder? {
            return try {
                val convertByte = result.source.source().readByteArray().convertSVGA()
                SVGADecoder(convertByte, options, imageLoader)
            } catch (e: Exception) {
                null
            }
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }

    companion object {
        const val REPEAT_COUNT_KEY = "svga#repeatCount"
        const val ANIMATION_START_CALLBACK_KEY = "svga#animation_start_callback"
        const val ANIMATION_END_CALLBACK_KEY = "svga#animation_end_callback"
        const val ANIMATION_FRAME_CALLBACK_KEY = "svga#animation_frame_callback"
    }
}