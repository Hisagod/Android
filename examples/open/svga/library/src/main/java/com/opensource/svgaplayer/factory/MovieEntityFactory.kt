package com.opensource.svgaplayer.factory

import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.log.LogUtils
import java.util.concurrent.ConcurrentHashMap

object MovieEntityFactory {
    private val pool = ConcurrentHashMap<String, MovieEntity>()
    private val count = ConcurrentHashMap<String, Int>()

    @Synchronized
    fun fetch(key: String, array: ByteArray): MovieEntity {
        val entity = pool[key]
        if (entity == null) {
            val newEntity = MovieEntity.ADAPTER.decode(array)
            pool[key] = newEntity
            count[key] = (count[key] ?: 0) + 1
            return newEntity
        }
        count[key] = (count[key] ?: 0) + 1
        return entity
    }

    fun size() {
        LogUtils.error(javaClass.simpleName, "缓存Pool池容量：${pool.size}个")
        LogUtils.error(javaClass.simpleName, "缓存Count池容量：${count.size}个")
    }

    @Synchronized
    fun clear(key: String): Boolean {
        val num = ((count[key] ?: 0) - 1).coerceAtLeast(0)
        if (num > 0) {
            count[key] = num
        }
        LogUtils.error(javaClass.simpleName, "剩余${num}个")
        if (num == 1) {
            pool.remove(key)
            count.remove(key)
        }
        return true
    }
}