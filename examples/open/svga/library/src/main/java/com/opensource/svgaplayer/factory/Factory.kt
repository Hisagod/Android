package com.opensource.svgaplayer.factory

class Factory {
    //    private val map = ConcurrentHashMap<String, D>()
    private val map = LinkedHashMap<String, Any>()
    private var size = 1000

    init {

    }

    fun put(key: String, value: Any) {
//        map[key] = value
        val cacheValue = map[key]
        if (cacheValue == null) {

        }

        map[key] = value
    }
}