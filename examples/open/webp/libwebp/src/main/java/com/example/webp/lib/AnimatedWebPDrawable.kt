package com.example.webp.lib

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import androidx.vectordrawable.graphics.drawable.Animatable2Compat

class AnimatedWebPDrawable(
    private val viewWidth: Int,
    private val viewHeight: Int,
    private val image: MutableList<LibWebPAnimatedDecoder.DecodeFrameResult>,
    private val loop: Int,
    private val frameCount: Int
) : Drawable(), Animatable2Compat {
    private val paint = Paint()
    private val TAG = javaClass.simpleName
    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()

    private var currentFrame = 0

    private var isAnimation = false

    private var animCount = 0

    private val nextFrame = {
        currentFrame += 1
        if (currentFrame >= frameCount) {
            currentFrame = 0
            animCount += 1
        }

        if (loop != 0) {
            if (animCount == loop) {
                stop()
            }
        }

        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        if (!isAnimation) {
            return
        }

        val time = SystemClock.uptimeMillis()
        image.get(currentFrame).let {
            canvas.drawBitmap(it.bitmap, 0f, 0f, paint)
            scheduleSelf(nextFrame, time + it.frameLengthMs)
        }
    }


    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    //传入绘制bitmap宽
    override fun getIntrinsicWidth(): Int {
        return viewWidth
    }

    //传入绘制bitmap高
    override fun getIntrinsicHeight(): Int {
        return viewHeight
    }

    override fun start() {
        Logger.e(TAG, "动画开始")

        callbacks.forEach { it.onAnimationStart(this) }
        isAnimation = true
    }

    /**
     * 单次播放动画结束回调
     */
    override fun stop() {
        Logger.e(TAG, "动画停止")
        callbacks.forEach { it.onAnimationEnd(this) }
        isAnimation = false
        unscheduleSelf(nextFrame)
    }

    override fun isRunning(): Boolean {
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