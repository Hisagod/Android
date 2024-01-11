package com.example.ponycui_home.svgaplayer

import android.app.Application
import coil.imageLoader

fun Application.getCustomInterceptor(): CustomInterceptor? {
    return imageLoader.components.interceptors.find { it is CustomInterceptor } as? CustomInterceptor
}