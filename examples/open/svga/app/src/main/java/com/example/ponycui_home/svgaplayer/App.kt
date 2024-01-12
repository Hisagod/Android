package com.example.ponycui_home.svgaplayer

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.util.DebugLogger
import coil.util.Logger
import com.opensource.svgaplayer.SVGA
import com.opensource.svgaplayer.SVGADecoder

class App : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        SVGA.enableLog(true)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
//            .crossfade(true)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(SVGADecoder.Factory())
                add(CustomInterceptor())
            }
            .logger(DebugLogger())
            .build()
    }
}