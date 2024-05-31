package com.opensource.svgaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.SoundPool
import androidx.collection.ArrayMap
import androidx.core.graphics.scale
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.Options
import com.opensource.svgaplayer.entities.SVGAAudioEntity
import com.opensource.svgaplayer.entities.SVGARect
import com.opensource.svgaplayer.entities.SVGAVideoSpriteEntity
import com.opensource.svgaplayer.factory.SVGARectFactory
import com.opensource.svgaplayer.proto.AudioEntity
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.utils.log.LogUtils
import com.opensource.svgaplayer.utils.svgaScale
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.math.roundToInt

/**
 * Created by PonyCui on 16/6/18.
 */
class SVGAVideoEntity(
    private val hashCode: String,
    private val svgaScale: Float?,
    private val audioPath: String,
    private val imageLoader: ImageLoader,
    val entity: MovieEntity
) {

    private val TAG = javaClass.simpleName

    //文本是否翻转，根据RTL布局进行设值
    var textFlip = false

    var antiAlias = true

    var videoSize = SVGARect(0.0, 0.0)

    var FPS = 15

    var frames: Int = 0

    internal var spriteList: MutableList<SVGAVideoSpriteEntity> = mutableListOf()

    //音频列表数据
    internal var audioList: MutableList<SVGAAudioEntity> = mutableListOf()

    //图片列表数据
    internal var imageMap = ArrayMap<String, Bitmap>()

    internal val scaleSize by lazy { svgaScale ?: 0.5f }

    init {
        setupByMovie(entity)
        parserImages(entity)
        resetSprites(entity)
//        parseAudio(entity)

//        LogUtils.error(TAG, scaleSize.toString())
    }

    fun setupByMovie(entity: MovieEntity) {
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

    fun parserImages(obj: MovieEntity) {
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

            val originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                .copy(Bitmap.Config.ARGB_8888, true)
            val scaleWidth = (originalBitmap.width.toFloat() * scaleSize).roundToInt()
            val scaleHeight = (originalBitmap.height.toFloat() * scaleSize).roundToInt()
            val scaleBitmap =
                originalBitmap.scale(scaleWidth, scaleHeight).copy(Bitmap.Config.ARGB_8888, true)

            //回收原始bitmap
            originalBitmap.recycle()

            imageMap[key] = scaleBitmap
            //存入内存缓存
            imageLoader.memoryCache?.set(
                MemoryCache.Key(memoryKey), MemoryCache.Value(scaleBitmap)
            )
        }
    }

    /**
     * 解析音频数据
     */
    fun parseAudio(soundPool: SoundPool, entity: MovieEntity) {
        val audiosFileMap = generateAudioFileMap(entity)
        audioList = entity.audios.map { audio ->
            createSvgaAudioEntity(soundPool, audio, audiosFileMap)
        }.toMutableList()
    }

    fun resetSprites(entity: MovieEntity) {
        spriteList = entity.sprites.map {
            SVGAVideoSpriteEntity(it)
        }.toMutableList()
    }

    private fun createSvgaAudioEntity(
        soundPool: SoundPool,
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
                item.sampleId = soundPool.load(
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
        LogUtils.error(TAG, "创建新音频文件")
        audioCache.createNewFile()
        FileOutputStream(audioCache).write(value)
        return audioCache
    }

    private fun generateAudioFileMap(entity: MovieEntity): ArrayMap<String, File> {
        val audiosFileMap = ArrayMap<String, File>()
        entity.images.entries.forEach {
            val imageKey = it.key
            val imageByteArray = it.value.toByteArray()
            if (imageByteArray.count() < 4) {
                return@forEach
            }
            val fileTag = imageByteArray.slice(IntRange(0, 3))
            val canSave =
                if (fileTag[0].toInt() == 73 && fileTag[1].toInt() == 68 && fileTag[2].toInt() == 51) {
                    true
                } else if (fileTag[0].toInt() == -1 && fileTag[1].toInt() == -5 && fileTag[2].toInt() == -108) {
                    true
                } else {
                    false
                }

            if (canSave) {
                val rootPath = "${audioPath}/svga"
                val rootFile = File(rootPath)
                if (!rootFile.exists()) {
                    rootFile.mkdir()
                }
                val audioPath = "${rootPath}/${hashCode}_${imageKey}.mp3"
                val audioCache = File(audioPath)
                audiosFileMap[imageKey] =
                    audioCache.takeIf { file -> file.exists() } ?: generateAudioFile(
                        audioCache,
                        imageByteArray
                    )
            }
        }
        return audiosFileMap
    }
}

