package com.github.skgmn.webpdecoder

import android.app.Application
import coil.imageLoader
import com.github.skgmn.webpdecoder.libwebp.CustomInterceptor

fun Application.getCustomInterceptor(): CustomInterceptor? {
    return imageLoader.components.interceptors.find { it is CustomInterceptor } as? CustomInterceptor
}