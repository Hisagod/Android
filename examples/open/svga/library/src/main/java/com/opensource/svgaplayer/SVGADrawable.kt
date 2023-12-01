package com.opensource.svgaplayer

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.blankj.utilcode.util.LogUtils
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer
import com.opensource.svgaplayer.utils.SVGAUtils

class SVGADrawable(val videoItem: SVGAVideoEntity) : Drawable(), Animatable, Drawable.Callback {

    var cleared = true
        internal set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidateSelf()
        }

    var currentFrame = 0
        internal set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidateSelf()
        }

    var scaleType: ImageView.ScaleType = ImageView.ScaleType.MATRIX
    var flip = false

    private var drawer: SVGACanvasDrawer? = null

    //首帧
    private var mStartFrame = 0

    //尾帧
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

    init {
        cleared = false
        scaleType = scaleType
        flip = flip

        //获取尾帧
        mEndFrame = Math.min(
            videoItem.frames - 1,
            (Int.MAX_VALUE) - 1
        )

        //开启动画监听前清理一遍
//        mAnimator?.cancel()
//        mAnimator?.removeAllListeners()
//        mAnimator?.removeAllUpdateListeners()

        val animator = ValueAnimator.ofInt(mStartFrame, mEndFrame)
        animator.interpolator = LinearInterpolator()
        animator.duration =
            ((mEndFrame - mStartFrame + 1) * (1000 / videoItem.FPS) / SVGAUtils.generateScale()).toLong()
        animator.repeatCount = if (loops <= 0) 99999 else loops - 1
        animator.addUpdateListener {
            currentFrame = animator?.animatedValue as Int
            val percentage =
                (currentFrame + 1).toDouble() / videoItem.frames.toDouble()
            onFrame?.invoke(currentFrame, percentage)

//            com.opensource.svgaplayer.utils.log.LogUtils.error(
//                javaClass.simpleName,
//                "当前帧数-》" + (it.animatedValue as Int).toString()
//            )
        }
        animator.addListener(onStart = {
            onStart?.invoke()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onStart"
            )
        }, onEnd = {
//            getSVGADrawable()?.let {
//                when (endAnimShow) {
//                    0 -> {
//                        drawable.currentFrame = mEndFrame
//                    }
//
//                    1 -> {
//                        drawable.currentFrame = mStartFrame
//                    }
//
//                    2 -> {
//                        drawable.cleared = true
//                    }
//                }
//            }
            onEnd?.invoke()
            stopAudio()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onEnd"
            )
        }, onCancel = {
            onCancel?.invoke()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onCancel"
            )
        }, onRepeat = {
            onRepeat?.invoke()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onRepeat"
            )
        })
        animator.addPauseListener(onPause = {
            onPause?.invoke()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onPause"
            )
        }, onResume = {
            onResume?.invoke()
            com.opensource.svgaplayer.utils.log.LogUtils.error(
                javaClass.simpleName,
                "SVGA动画--》onResume"
            )
        })
//        if (reverse) {
//            animator.reverse()
//        } else {
//        animator.start()
//        }
        mAnimator = animator

        //Coil加载非match_parent控件，会回调GenericViewTarget的onSuccess
//        visibilityChanged(visibility)

        drawer = SVGACanvasDrawer(videoItem, SVGADynamicEntity())
    }

    override fun draw(canvas: Canvas) {
        if (cleared) {
            return
        }
        drawer?.drawFrame(canvas, currentFrame, scaleType, flip)
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
        LogUtils.e("start")
        mAnimator?.start()
    }

    override fun stop() {
        LogUtils.e("stop")
        mAnimator?.pause()
    }

    override fun isRunning(): Boolean {
        return mAnimator?.isRunning ?: false
    }

    override fun invalidateDrawable(who: Drawable) = invalidateSelf()

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) =
        scheduleSelf(what, `when`)

    override fun unscheduleDrawable(who: Drawable, what: Runnable) = unscheduleSelf(what)
}