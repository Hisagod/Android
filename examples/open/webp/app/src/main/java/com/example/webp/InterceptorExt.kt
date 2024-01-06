package com.example.webp

import android.app.Application
import coil.imageLoader

fun Application.getCustomInterceptor(): com.example.webp.CustomInterceptor? {
    return imageLoader.components.interceptors.find { it is com.example.webp.CustomInterceptor } as? com.example.webp.CustomInterceptor
}