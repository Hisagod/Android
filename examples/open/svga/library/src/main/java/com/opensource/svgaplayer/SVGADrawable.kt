package com.opensource.svgaplayer

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.SystemClock
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.ImageLoader
import coil.request.Options
import coil.size.Scale
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer
import com.opensource.svgaplayer.utils.isLayoutRtl
import com.opensource.svgaplayer.utils.log.LogUtils
import com.opensource.svgaplayer.utils.svgaAnimationEndCallback
import com.opensource.svgaplayer.utils.svgaAnimationFrameCallback
import com.opensource.svgaplayer.utils.svgaAnimationRepeatCallback
import com.opensource.svgaplayer.utils.svgaAnimationStartCallback
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaRepeatCount
import com.opensource.svgaplayer.utils.svgaRtl

class SVGADrawable(
    private val key: String,
    private val videoItem: SVGAVideoEntity,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Drawable(), Animatable2Compat {
    private val TAG = javaClass.simpleName

    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()

    @Volatile
    private var currentFrame = 0

    //控制动画的位置
    private val scaleType: Scale = options.scale

    private var drawer = SVGACanvasDrawer(videoItem)

    //每帧时长
    private var frameTime: Long = 0

    //记录需要播放次数 0无限次 1播放单次
    private var loop = options.parameters.svgaRepeatCount()

    //记录播放几次
    private var loopCount = 0

    @Volatile
    private var isAnimation = false

    private val soundPool by lazy { getSoundPool(20) }

    private val svgaRtlEntity by lazy { options.parameters.svgaRtl() }
    private val svgaDynamicEntity by lazy { options.parameters.svgaDynamicEntity() }

    private val nextFrame = {
//        LogUtils.error(TAG, "nextFrame")

        //取出下一帧位
        currentFrame += 1
        if (currentFrame >= videoItem.frames) {
            currentFrame = 0
            loopCount += 1

            //重复执行回调
            options.parameters.svgaAnimationRepeatCallback()?.invoke()
        }

        if (loop != 0) {
            //不是无限播放，有播放次数限制
            if (loopCount == loop) {
                isAnimation = false
                stop()
            }
        }

        if (isAnimation) {
            invalidateSelf()
        }
    }

    init {
        LogUtils.error(TAG, "初始化Drawable")
        val frame = videoItem.frames.toFloat()
        val fps = videoItem.FPS.toFloat()
        val duration = frame.div(fps) * 1000
        frameTime = (duration / frame).toLong()
//        frameTime = 500

        svgaRtlEntity?.let {
            if (it.viewRtl.isLayoutRtl()) {
                it.viewRtl.scaleX = -1f
                videoItem.textFlip = true
            }
        }

        setSVGADynamicEntity(svgaDynamicEntity)

        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
//            LogUtils.error(TAG, "音频${sampleId}加载完成")

            videoItem.audioList.forEach {
                if (it.sampleId == sampleId) {
                    it.isLoadComplete = true
                }
            }

            val complete = videoItem.audioList.map { it.isLoadComplete }
            if (complete.contains(false)) {
                return@setOnLoadCompleteListener
            }

            if (status == 0) {
                isAnimation = true
                options.parameters.svgaAnimationStartCallback()?.invoke()
                callbacks.forEach { it.onAnimationStart(this) }

                invalidateSelf()
            }
        }


//        SVGAAudioManager.onFinish {
//            LogUtils.error(TAG, "准备播放音视频动画")
//            val contain = SVGAAudioManager.pool.contains(it)
//            if (contain) {
//                isAnimation = true
//                onStart?.invoke()
//                callbacks.forEach { it.onAnimationStart(this) }
//
//                invalidateSelf()
//            }
//        }
    }

    override fun draw(canvas: Canvas) {
//        LogUtils.error(TAG, "canvas")
        if (!isAnimation) {
            return
        }

//        LogUtils.error(TAG, "canvas--frame:${currentFrame}")
        val time = SystemClock.uptimeMillis()
        drawer.drawFrame(canvas, currentFrame, scaleType)
        playAudio(currentFrame)
//        LogUtils.error(TAG, currentFrame.toString())
        options.parameters.svgaAnimationFrameCallback()?.invoke(currentFrame)
        scheduleSelf(nextFrame, time + frameTime)
    }


    override fun setAlpha(alpha: Int) {

    }


    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    //修改元素
    fun setSVGADynamicEntity(de: SVGADynamicEntity?) {
        drawer.setSVGADynamicEntity(de)
    }

    fun getSVGADynamicEntity(): SVGADynamicEntity? {
        return drawer.getSVGADynamicEntity()
    }

    fun stepToFrame(frame: Int) {
        if (frame < 0) return
        if (frame >= videoItem.frames) return
        currentFrame = frame
    }

    private fun playAudio(frameIndex: Int) {
        videoItem.audioList.forEach { audio ->
            if (audio.startFrame == frameIndex) {
                audio.sampleId?.let {
                    audio.playID = soundPool.play(it, 1f, 1f, 1, 0, 1f)
                }
            }

//            if (audio.endFrame <= frameIndex) {
//                audio.playID?.let {
////                    SVGAAudioManager.stop(it)
//                    SVGAAudioManager.pause(it)
//                }
////                audio.playID = null
//            }
        }
    }

    override fun start() {
        LogUtils.error(TAG, "${key}-start")
        if (videoItem.entity.audios.isEmpty()) {
            //无音频直接播放

            isAnimation = true
            options.parameters.svgaAnimationStartCallback()?.invoke()
            callbacks.forEach { it.onAnimationStart(this) }

            invalidateSelf()
        } else {
            //有音频需要等待音频加载完毕在执行
            videoItem.parseAudio(soundPool, videoItem.entity)
        }

//        isAnimation = true
//        onStart?.invoke()
//        callbacks.forEach { it.onAnimationStart(this) }
    }

    override fun stop() {
        LogUtils.error(TAG, "${key}-stop")
        isAnimation = false
        callbacks.forEach { it.onAnimationEnd(this) }
        unscheduleSelf(nextFrame)
        options.parameters.svgaAnimationEndCallback()?.invoke()

        videoItem.audioList.forEach {
            it.sampleId?.let {
                soundPool.stop(it)
                soundPool.unload(it)
            }
        }
    }

    override fun isRunning(): Boolean {
        LogUtils.error(TAG, "动画isRunning")
        return isAnimation
    }

    override fun registerAnimationCallback(callback: Animatable2Compat.AnimationCallback) {
        if (callback !in callbacks) {
            callbacks += callback
        }
    }

    override fun unregisterAnimationCallback(callback: Animatable2Compat.AnimationCallback): Boolean {
        return if (callback in callbacks) {
            callbacks -= callback
            true
        } else {
            false
        }
    }

    override fun clearAnimationCallbacks() {
        callbacks.clear()
    }

    private fun getSoundPool(maxStreams: Int) = if (Build.VERSION.SDK_INT >= 21) {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        SoundPool.Builder().setAudioAttributes(attributes)
            .setMaxStreams(maxStreams)
            .build()
    } else {
        SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0)
    }

}