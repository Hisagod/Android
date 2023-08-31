package com.opensource.svgaplayer

import coil.ImageLoader
import coil.decode.BitmapFactoryDecoder
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.fetch.SourceResult
import coil.request.Options
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.inflate
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGADecoder(private val source: ByteArray) : Decoder {
    override suspend fun decode(): DecodeResult {
        LogUtils.error("SVGADecoder", "SVGA-》decode成功")
        val mEntity = MovieEntity.ADAPTER.decode(source)
        val entity = SVGAVideoEntity(mEntity)
        return DecodeResult(SVGADrawable(entity), true)
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder? {
            LogUtils.error("SVGADecoder", result.mimeType.toString())
            if (result.mimeType == null) {
                val byteArray = result.source.source().readByteArray()
                val inflate = byteArray.inflate()
                return SVGADecoder(inflate)
            } else {
                return null
            }
        }
    }
}