package com.opensource.svgaplayer

import coil.ImageLoader
import coil.decode.BitmapFactoryDecoder
import coil.decode.DecodeResult
import coil.decode.DecodeUtils
import coil.decode.Decoder
import coil.decode.ImageDecoderDecoder
import coil.decode.isAnimatedHeif
import coil.decode.isAnimatedWebP
import coil.decode.isGif
import coil.decode.isHeif
import coil.decode.isSvg
import coil.decode.isWebP
import coil.fetch.SourceResult
import coil.request.Options
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.inflate
import com.opensource.svgaplayer.utils.isSVGA
import com.opensource.svgaplayer.utils.log.LogUtils
import okio.ByteString.Companion.encodeUtf8

class SVGADecoder(private val array: ByteArray) : Decoder {
    override suspend fun decode(): DecodeResult {
        LogUtils.error("SVGADecoder", "SVGA解析成功")
        val mEntity = MovieEntity.ADAPTER.decode(array)
        val entity = SVGAVideoEntity(mEntity)
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
    }
}