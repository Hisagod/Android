package com.opensource.svgaplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.ArrayMap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.executeBlocking
import coil.imageLoader
import coil.request.ImageRequest
import com.blankj.utilcode.constant.MemoryConstants
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.opensource.svgaplayer.entities.SVGAAudioEntity
import com.opensource.svgaplayer.entities.SVGAVideoSpriteEntity
import com.opensource.svgaplayer.proto.AudioEntity
import com.opensource.svgaplayer.proto.MovieEntity
import com.opensource.svgaplayer.proto.MovieParams
import com.opensource.svgaplayer.transform.MirrorTransform
import com.opensource.svgaplayer.utils.PathUtils
import com.opensource.svgaplayer.utils.SVGARect
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by PonyCui on 16/6/18.
 */
class SVGAVideoEntity {

    private val TAG = "SVGAVideoEntity"

    var antiAlias = true
    var movieItem: MovieEntity? = null

    var videoSize = SVGARect(0.0, 0.0, 0.0, 0.0)
        private set

    var FPS = 15
        private set

    var frames: Int = 0
        private set

    internal var spriteList: MutableList<SVGAVideoSpriteEntity> = mutableListOf()
    internal var audioList: MutableList<SVGAAudioEntity> = mutableListOf()
    private var soundCallback: SVGASoundManager.SVGASoundCallBack? = null
    internal var imageMap = ArrayMap<String, Bitmap>()

    constructor(entity: MovieEntity) {
        this.movieItem = entity
        entity.params?.let(this::setupByMovie)
        parserImages(entity)
        resetSprites(entity)
    }

    private fun setupByMovie(movieParams: MovieParams) {
        val width = (movieParams.viewBoxWidth ?: 0.0f).toDouble()
        val height = (movieParams.viewBoxHeight ?: 0.0f).toDouble()
        videoSize = SVGARect(0.0, 0.0, width, height)
        FPS = movieParams.fps ?: 20
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

            val opt = BitmapFactory.Options()
            opt.inBitmap = null
            opt.inMutable = true
            opt.inSampleSize = 2
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, opt)
//            val bitmap = createBitmap(byteArray)
            imageMap[entry.key] = bitmap
        }
    }

    private fun createBitmap(byteArray: ByteArray): Bitmap {
        return Glide.with(SVGA.app)
            .asBitmap()
            .load(byteArray)
//            .override((videoSize.width*0.5).toInt(), (videoSize.height*0.5).toInt())
            .submit()
            .get()
    }

    private fun resetSprites(entity: MovieEntity) {
        spriteList = entity.sprites.map {
            SVGAVideoSpriteEntity(it)
        }.toMutableList()
    }

    fun setupAudios(onComplete: () -> Unit) {
        movieItem?.let { entity ->
            //不带音频SVGA
            if (entity.audios == null || entity.audios.isEmpty()) {
                onComplete.invoke()
                return
            }

            //以音频名为key，file为value包装成map
            val audiosFileMap = generateAudioFileMap(entity)
            //本地音频文件为空（可能用户会删除数据），也播放
            if (audiosFileMap.size == 0) {
                onComplete.invoke()
                return
            }

            //带音频效果SVGA
            setupSoundPool(entity) {
                onComplete.invoke()
            }

            audioList = entity.audios.map { audio ->
                createSvgaAudioEntity(audio, audiosFileMap)
            }.toMutableList()
        }
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
        spriteList.clear()
        imageMap.forEach {
            it.value.recycle()
        }
        imageMap.clear()
    }
}

