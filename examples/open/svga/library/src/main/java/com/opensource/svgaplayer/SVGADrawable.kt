package com.opensource.svgaplayer

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.ImageLoader
import coil.request.Options
import coil.request.animationEndCallback
import coil.request.animationStartCallback
import coil.request.repeatCount
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer
import com.opensource.svgaplayer.utils.SVGAUtils
import com.opensource.svgaplayer.utils.log.LogUtils
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SVGADrawable(
    private val videoItem: SVGAVideoEntity,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Drawable(), Animatable2Compat {
    private val TAG = javaClass.simpleName

    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()

    private var currentFrame = 1

    //控制动画的位置
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER

    //控制阿语环境，动画水平反转
    private var flip = false

    private var drawer: SVGACanvasDrawer? = null


    //    private var onStart: (() -> Unit)? = null
//    private var onEnd: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onRepeat: (() -> Unit)? = null
    private var onPause: (() -> Unit)? = null
    private var onResume: (() -> Unit)? = null
    private var onFrame: ((frame: Int, percentage: Double) -> Unit)? = null

    private val onStart = options.parameters.animationStartCallback()

    private val onEnd = options.parameters.animationEndCallback()

    //每帧时长
    private var frameTime: Long = 0

    //记录需要播放次数 0无限次 1播放单次
    private var loop = options.parameters.repeatCount()

    //记录播放几次
    private var loopCount = 0

    @Volatile
    private var isAnimation = false

    private val nextFrame = {
//        LogUtils.error(TAG, "nextFrame")

        //取出下一帧位
        currentFrame += 1
        if (currentFrame > videoItem.frames) {
            currentFrame = 1
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
    }

    override fun draw(canvas: Canvas) {
//        LogUtils.error(TAG, "canvas")
        val time = SystemClock.uptimeMillis()
        drawer?.drawFrame(canvas, currentFrame, scaleType, flip)
//        LogUtils.error(TAG, currentFrame.toString())

        scheduleSelf(nextFrame, time + frameTime)
    }


    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    fun resumeAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.resume(it)
            }
        }
    }

    fun pauseAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.pause(it)
            }
        }
    }

    fun stopAudio() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.stop(it)
            }
        }
    }

    fun clear() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.stop(it)
            }
            audio.playID = null
        }

        drawer?.onClear()

        getDynamicItem().clearDynamicObjects()

        videoItem.clear()
    }

    fun setDynamicItem(dynamicItem: SVGADynamicEntity) {
        drawer = SVGACanvasDrawer(videoItem, dynamicItem)
    }

    fun getDynamicItem(): SVGADynamicEntity {
        return drawer?.dynamicItem ?: SVGADynamicEntity()
    }

    override fun start() {
        LogUtils.error(TAG, "动画start")
        if (isAnimation) return
        isAnimation = true

        onStart?.invoke()
        callbacks.forEach { it.onAnimationStart(this) }


        val frame = videoItem.frames.toFloat()
        LogUtils.error(TAG, "动画总帧数：${frame}")
        val fps = videoItem.FPS.toFloat()
        val duration = frame.div(fps) * 1000
        frameTime = (duration / frame).toLong()
        LogUtils.error(TAG, "单帧时长：${frameTime}")

//        invalidateSelf()

//        mEndFrame = Math.min(videoItem.frames - 1, (Int.MAX_VALUE) - 1)
//        val animator = ValueAnimator.ofInt(mStartFrame, mEndFrame)
//        animator.interpolator = LinearInterpolator()
//        animator.duration =
//            ((mEndFrame - mStartFrame + 1) * (1000 / videoItem.FPS) / SVGAUtils.generateScale()).toLong()
//        animator.repeatCount = if (loops <= 0) 99999 else loops - 1
//        animator.addUpdateListener {
//            currentFrame = animator?.animatedValue as Int
//            val percentage =
//                (currentFrame + 1).toDouble() / videoItem.frames.toDouble()
//            onFrame?.invoke(currentFrame, percentage)
//        }
//        animator.addListener(onStart = {
//            onStart?.invoke()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onStart"
//            )
//        }, onEnd = {
//            onEnd?.invoke()
//            stopAudio()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onEnd"
//            )
//        }, onCancel = {
//            onCancel?.invoke()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onCancel"
//            )
//        }, onRepeat = {
//            onRepeat?.invoke()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onRepeat"
//            )
//        })
//        animator.addPauseListener(onPause = {
//            onPause?.invoke()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onPause"
//            )
//        }, onResume = {
//            onResume?.invoke()
//            LogUtils.error(
//                javaClass.simpleName,
//                "SVGA动画--》onResume"
//            )
//        })
//
        drawer = SVGACanvasDrawer(videoItem, SVGADynamicEntity())
//        animator.start()
//        mAnimator = animator
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
}