package com.opensource.svgaplayer

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.ImageLoader
import coil.request.Options
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGADrawable(
    private val videoItem: SVGAVideoEntity,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Drawable(), Animatable2Compat/*, LifecycleObserver */ {
    private val TAG = javaClass.simpleName

    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()

    private var currentFrame = 0

    //控制动画的位置
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER

    //控制阿语环境，动画水平反转
    private var flip = false

    private var drawer = SVGACanvasDrawer(videoItem)

    private var onCancel: (() -> Unit)? = null
    private var onRepeat: (() -> Unit)? = null
    private var onPause: (() -> Unit)? = null
    private var onResume: (() -> Unit)? = null

    private var onFrame = options.parameters.svgaAnimationFrameCallback()

    private val onStart = options.parameters.svgaAnimationStartCallback()

    private val onEnd = options.parameters.svgaAnimationEndCallback()

    //每帧时长
    private var frameTime: Long = 0

    //记录需要播放次数 0无限次 1播放单次
    private var loop = options.parameters.svgaRepeatCount()

    //记录播放几次
    private var loopCount = 0

    @Volatile
    private var isAnimation = false

    private val nextFrame = {
//        LogUtils.error(TAG, "nextFrame")

        //取出下一帧位
        currentFrame += 1
        if (currentFrame > videoItem.frames) {
            currentFrame = 0
            loopCount += 1
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
        val frame = videoItem.frames.toFloat()
        val fps = videoItem.FPS.toFloat()
        val duration = frame.div(fps) * 1000
        frameTime = (duration / frame).toLong()
    }

    override fun draw(canvas: Canvas) {
//        LogUtils.error(TAG, "canvas")
        if (!isAnimation) {
            return
        }
        val time = SystemClock.uptimeMillis()
        drawer.drawFrame(canvas, currentFrame, scaleType, flip)
//        LogUtils.error(TAG, currentFrame.toString())
        onFrame?.invoke(currentFrame)

        scheduleSelf(nextFrame, time + frameTime)
    }


    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    fun updateSVGADynamicEntity(de: SVGADynamicEntity) {
        drawer.updateSVGADynamicEntity(de)
    }

    private fun resumeAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.resume(it)
            }
        }
    }

    private fun pauseAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.pause(it)
            }
        }
    }

    private fun stopAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.stop(it)
            }
        }
    }

    override fun start() {
        LogUtils.error(TAG, "动画start")
        isAnimation = true
        onStart?.invoke()
        callbacks.forEach { it.onAnimationStart(this) }
    }

    override fun stop() {
        LogUtils.error(TAG, "动画stop")
        isAnimation = false
        callbacks.forEach { it.onAnimationEnd(this) }
        unscheduleSelf(nextFrame)
        onEnd?.invoke()
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

//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun onResume(owner: LifecycleOwner) {
//        resumeAudio()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    private fun onStop(owner: LifecycleOwner) {
//        pauseAudio()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    private fun onDestroy(owner: LifecycleOwner) {
//        stopAudio()
//        SVGASoundManager.release()
//    }
}