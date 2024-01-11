package com.example.ponycui_home.svgaplayer

import coil.intercept.Interceptor
import coil.request.ImageRequest
import coil.request.ImageResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

open class CustomInterceptor : Interceptor {
    val loaderFlow = MutableStateFlow(true)
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val imageRequest = chain.request
        loaderFlow.first { it }
        return chain.proceed(imageRequest)
    }
}