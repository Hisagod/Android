package com.opensource.svgaplayer

import coil.ImageLoader
import coil.decode.DecodeResult
import coil.decode.DecodeUtils
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.proto.MovieEntityFactory
import com.opensource.svgaplayer.utils.inflate
import com.opensource.svgaplayer.utils.isSVGA
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGADecoder(private val array: ByteArray) : Decoder {
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
        return DecodeResult(SVGADrawable(entity), false)
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
                SVGADecoder(inflate)
            } else {
                null
            }
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }
}