package com.opensource.svgaplayer

import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.DecodeUtils
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import coil.request.animatedTransformation
import coil.request.animationEndCallback
import coil.request.animationStartCallback
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.proto.MovieEntityFactory
import com.opensource.svgaplayer.utils.inflate
import com.opensource.svgaplayer.utils.isSVGA
import com.opensource.svgaplayer.utils.log.LogUtils

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
        val entity = SVGAVideoEntity(movieEntity)
        val drawable = SVGADrawable(entity, options, imageLoader)

//        val onStart = options.parameters.animationStartCallback()
//        val onEnd = options.parameters.animationEndCallback()
//        if (onStart != null || onEnd != null) {
//            drawable.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
//                override fun onAnimationStart(drawable: Drawable?) {
//                    super.onAnimationStart(drawable)
//                    onStart?.invoke()
//                }
//
//                override fun onAnimationEnd(drawable: Drawable?) {
//                    super.onAnimationEnd(drawable)
//                    onEnd?.invoke()
//                }
//            })
//        }
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
            return if (DecodeUtils.isSVGA(source)) {
                val byteArray = source.readByteArray()
                val inflate = byteArray.inflate()
                SVGADecoder(inflate, options, imageLoader)
            } else {
                null
            }
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }
}