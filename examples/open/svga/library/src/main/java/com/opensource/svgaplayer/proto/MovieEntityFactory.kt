package com.opensource.svgaplayer.proto

import java.util.concurrent.ConcurrentHashMap

object MovieEntityFactory {
    private val pool = ConcurrentHashMap<String, MovieEntity>()

    fun put(key: String, value: MovieEntity) {
        pool.put(key, value)
    }

    fun get(key: String): MovieEntity? {
        return pool.get(key)
    }
}