package com.opensource.svgaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import coil.ImageLoader
import coil.memory.MemoryCache
import com.opensource.svgaplayer.entities.SVGAAudioEntity
import com.opensource.svgaplayer.entities.SVGARect
import com.opensource.svgaplayer.entities.SVGAVideoSpriteEntity
import com.opensource.svgaplayer.proto.AudioEntity
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.PathUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by PonyCui on 16/6/18.
 */
class SVGAVideoEntity(
    private val hashCode: String,
    private val imageLoader: ImageLoader,
    private val entity: MovieEntity
) {

    private val TAG = "SVGAVideoEntity"

    var antiAlias = true

    var videoSize = SVGARect(0.0, 0.0)
        private set

    var FPS = 15
        private set

    var frames: Int = 0
        private set

    internal var spriteList: MutableList<SVGAVideoSpriteEntity> = mutableListOf()

    //音频列表数据
    internal var audioList: MutableList<SVGAAudioEntity> = mutableListOf()
    private var soundCallback: SVGASoundManager.SVGASoundCallBack? = null

    //图片列表数据
    internal var imageMap = ArrayMap<String, Bitmap>()

    init {
        setupByMovie(entity)
        parserImages(entity)
        parseAudio(entity)
        resetSprites(entity)
    }

    private fun setupByMovie(entity: MovieEntity) {
        val movieParams = entity.params

        //画布宽度
        val width = (movieParams.viewBoxWidth ?: 0.0f).toDouble()
        //画布高度
        val height = (movieParams.viewBoxHeight ?: 0.0f).toDouble()

        videoSize = SVGARect(width, height)

        //动画每秒帧数
        FPS = movieParams.fps ?: 15
        //动画总帧数
        frames = movieParams.frames ?: 0
    }

    private fun parserImages(obj: MovieEntity) {
        obj.images?.entries?.forEach { entry ->
            val byteArray = entry.value.toByteArray()
            if (byteArray.count() < 4) {
                return@forEach
            }
            val fileTag = byteArray.slice(IntRange(0, 3))
            if (fileTag[0].toInt() == 73 && fileTag[1].toInt() == 68 && fileTag[2].toInt() == 51) {
                return@forEach
            }

            val key = entry.key
            //生成key
            val memoryKey = hashCode + key
            //从缓存拿
            val value = imageLoader.memoryCache?.get(MemoryCache.Key(memoryKey))
            val cacheBitmap = value?.bitmap
            if (cacheBitmap != null) {
                imageMap[key] = cacheBitmap
                return@forEach
            }

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imageMap[key] = bitmap
            //存入内存缓存
            imageLoader.memoryCache?.set(
                MemoryCache.Key(memoryKey), MemoryCache.Value(bitmap)
            )
        }
    }

    /**
     * 解析音频数据
     */
    private fun parseAudio(entity: MovieEntity) {
        //以音频名为key，file为value包装成map
        val audiosFileMap = generateAudioFileMap(entity)
        //带音频效果SVGA
        setupSoundPool(entity) {

        }
        audioList = entity.audios.map { audio ->
            createSvgaAudioEntity(audio, audiosFileMap)
        }.toMutableList()
    }

    private fun resetSprites(entity: MovieEntity) {
        spriteList = entity.sprites.map {
            SVGAVideoSpriteEntity(it)
        }.toMutableList()
    }

    private fun createSvgaAudioEntity(
        audio: AudioEntity,
        audiosFileMap: ArrayMap<String, File>
    ): SVGAAudioEntity {
        val item = SVGAAudioEntity(audio)
        val startTime = (audio.startTime ?: 0).toDouble()
        val totalTime = (audio.totalTime ?: 0).toDouble()
        if (totalTime.toInt() == 0) {
            // 除数不能为 0
            return item
        }
        audiosFileMap[audio.audioKey]?.let { file ->
            FileInputStream(file).use {
                val length = it.available().toDouble()
                val offset = ((startTime / totalTime) * length).toLong()
                item.soundID = SVGASoundManager.load(
                    soundCallback,
                    it.fd,
                    offset,
                    length.toLong(),
                    1
                )
            }
        }
        return item
    }

    private fun generateAudioFile(audioCache: File, value: ByteArray): File {
        audioCache.createNewFile()
        FileOutputStream(audioCache).write(value)
        return audioCache
    }

    private fun generateAudioFileMap(entity: MovieEntity): ArrayMap<String, File> {
        val audiosDataMap = generateAudioMap(entity)
        val audiosFileMap = ArrayMap<String, File>()
        if (audiosDataMap.isNotEmpty()) {
            audiosDataMap.forEach {
                val audioCache = PathUtils.getAudioFile(it.key)
                audiosFileMap[it.key] =
                    audioCache.takeIf { file -> file.exists() } ?: generateAudioFile(
                        audioCache,
                        it.value
                    )
            }
        }
        return audiosFileMap
    }

    private fun generateAudioMap(entity: MovieEntity): ArrayMap<String, ByteArray> {
        val audiosDataMap = ArrayMap<String, ByteArray>()
        entity.images?.entries?.forEach {
            val imageKey = it.key
            val byteArray = it.value.toByteArray()
            if (byteArray.count() < 4) {
                return@forEach
            }
            val fileTag = byteArray.slice(IntRange(0, 3))
            if (fileTag[0].toInt() == 73 && fileTag[1].toInt() == 68 && fileTag[2].toInt() == 51) {
                audiosDataMap[imageKey] = byteArray
            } else if (fileTag[0].toInt() == -1 && fileTag[1].toInt() == -5 && fileTag[2].toInt() == -108) {
                audiosDataMap[imageKey] = byteArray
            }
        }
        return audiosDataMap
    }

    private fun setupSoundPool(
        entity: MovieEntity,
        onComplete: () -> Unit
    ) {
        var soundLoaded = 0
        soundCallback = object : SVGASoundManager.SVGASoundCallBack {
            override fun onVolumeChange(value: Float) {
                SVGASoundManager.setVolume(value, this@SVGAVideoEntity)
            }

            override fun onComplete() {
                soundLoaded++
                if (soundLoaded >= entity.audios.count()) {
                    onComplete.invoke()
                }
            }
        }
    }

    fun clear() {
        audioList.forEach {
            it.soundID?.let { id -> SVGASoundManager.unload(id) }
        }
        SVGASoundManager.release()

        audioList.clear()
        audioList = mutableListOf()

        spriteList.forEach {
            it.clear()
        }
        spriteList.clear()
        spriteList = mutableListOf()

        imageMap.forEach {
            it.value.recycle()
        }
        imageMap.clear()
        imageMap = arrayMapOf()
    }
}

