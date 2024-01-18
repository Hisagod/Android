package com.example.webp.lib

import android.graphics.Bitmap
import android.os.Build
import androidx.core.graphics.scale
import coil.ImageLoader
import coil.memory.MemoryCache
import java.nio.ByteBuffer

class LibWebPAnimatedDecoder private constructor(
    // to keep in memory
    @Suppress("unused")
    private val byteBuffer: ByteBuffer,
    private val decoder: Long,
    private val premultipliedAlpha: Boolean
) {
    private val TAG = javaClass.simpleName

    val width get() = metadata.width
    val height get() = metadata.height
    val loopCount get() = metadata.loopCount
    val frameCount get() = metadata.frameCount

    private val metadata by lazy(LazyThreadSafetyMode.NONE) { getMetadata(decoder) }

    //imageLoader是coil框架加载器，可拿到内存缓存对象和本地缓存对象
    fun decodeNextFrame(
        index: Int,
        hashId: String,
        scaleWidth: Int,
        scaleHeight: Int,
        imageLoader: ImageLoader
    ): DecodeFrameResult? {
        //生成key
        val key = hashId + index
        //从缓存拿
        val value = imageLoader.memoryCache?.get(MemoryCache.Key(key))
        val cacheBitmap = value?.bitmap
        val cacheDuration = value?.extras?.get("duration") as? Int ?: 0
        if (cacheBitmap != null) {
            if (cacheDuration >= 0) {
                return DecodeFrameResult(cacheBitmap, cacheDuration)
            } else {
                return null
            }
        }

        //创建新bitmap
        val originalBitmap =
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (originalBitmap.isPremultiplied != premultipliedAlpha) {
                originalBitmap.isPremultiplied = premultipliedAlpha
            }
        }
        //读取下一帧数据到新bitmap
        val duration = decodeNextFrame(decoder, originalBitmap)

        //缩放处理
        val scaleBitmap =
            originalBitmap.scale(scaleWidth, scaleHeight).copy(Bitmap.Config.ARGB_8888, true)

        //回收原始数据
        originalBitmap.recycle()

        return if (duration >= 0) {
            //存入内存缓存
            imageLoader.memoryCache?.set(
                MemoryCache.Key(key), MemoryCache.Value(
                    scaleBitmap,
                    mapOf(("duration" to duration))
                )
            )
            DecodeFrameResult(scaleBitmap, duration)
        } else {
            null
        }
    }

    fun hasNextFrame(): Boolean {
        return hasNextFrame(decoder)
    }

    fun reset() {
        reset(decoder)
    }

    protected fun finalize() {
        deleteDecoder(decoder)
    }

    class DecodeFrameResult(
        val bitmap: Bitmap,
        val frameLengthMs: Int
    )

    private class Metadata(
        val width: Int,
        val height: Int,
        val loopCount: Int,
        val frameCount: Int
    )

    companion object {
        @JvmStatic
        private external fun createDecoder(
            byteBuffer: ByteBuffer,
            premultipliedAlpha: Boolean
        ): Long

        @JvmStatic
        private external fun deleteDecoder(decoder: Long)

        @JvmStatic
        private external fun getMetadata(decoder: Long): Metadata

        @JvmStatic
        private external fun hasNextFrame(decoder: Long): Boolean

        @JvmStatic
        private external fun decodeNextFrame(decoder: Long, outBitmap: Bitmap): Int

        @JvmStatic
        private external fun reset(decoder: Long)

        init {
            System.loadLibrary("libwebp")
        }

        fun create(
            diskCacheKey: String,
            byteBuffer: ByteBuffer,
            premultipliedAlpha: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        ): LibWebPAnimatedDecoder {

//            val instance = LibWebPAnimatedCache.get(diskCacheKey)
//
//            if (instance != null) {
//                return instance
//            }

            val directBuffer = if (byteBuffer.isDirect) {
                byteBuffer
            } else {
                ByteBuffer.allocateDirect(byteBuffer.limit()).put(byteBuffer)
            }

            val create = LibWebPAnimatedDecoder(
                directBuffer,
                createDecoder(
                    directBuffer,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && premultipliedAlpha
                ),
                premultipliedAlpha
            )

//            LibWebPAnimatedCache.put(diskCacheKey, create)

            return create
        }
    }
}