package com.example.webp.lib

import java.util.concurrent.ConcurrentHashMap

object LibWebPAnimatedCache {
    val cache = ConcurrentHashMap<String, LibWebPAnimatedDecoder>()

    fun put(key: String, value: LibWebPAnimatedDecoder) {
        cache[key] = value
    }

    fun get(key: String): LibWebPAnimatedDecoder? {
        return cache[key]
    }
}