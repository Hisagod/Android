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
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer
import com.opensource.svgaplayer.utils.SVGAUtils
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGADrawable(private val videoItem: SVGAVideoEntity) : Drawable(), Animatable2Compat {
    private val TAG = javaClass.simpleName

    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()

    private var currentFrame = 0
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidateSelf()
        }

    //控制动画的位置
    private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER

    //控制阿语环境，动画水平反转
    private var flip = false

    private var drawer: SVGACanvasDrawer? = null

    //属性动画前值
    private var mStartFrame = 0

    //属性动画后值
    private var mEndFrame = 0

    private var mAnimator: ValueAnimator? = null

    private var onStart: (() -> Unit)? = null
    private var onEnd: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onRepeat: (() -> Unit)? = null
    private var onPause: (() -> Unit)? = null
    private var onResume: (() -> Unit)? = null
    private var onFrame: ((frame: Int, percentage: Double) -> Unit)? = null
    var loops = 0

    private val nextFrame = {

    }

    override fun draw(canvas: Canvas) {
//        drawer?.drawFrame(canvas, currentFrame, scaleType, flip)
//        LogUtils.error(TAG, "canvas")

        scheduleSelf(nextFrame, 10000)
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

        callbacks.forEach { it.onAnimationStart(this) }

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
//        drawer = SVGACanvasDrawer(videoItem, SVGADynamicEntity())
//        animator.start()
//        mAnimator = animator
    }

    override fun stop() {
        LogUtils.error(TAG, "动画stop")
        mAnimator?.pause()

        callbacks.forEach { it.onAnimationEnd(this) }
    }

    override fun isRunning(): Boolean {
        LogUtils.error(TAG, "动画isRunning")
        return mAnimator?.isRunning ?: false
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