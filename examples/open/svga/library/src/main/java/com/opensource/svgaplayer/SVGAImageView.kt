package com.opensource.svgaplayer

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import com.opensource.svgaplayer.utils.SVGARange
import com.opensource.svgaplayer.utils.ViewUtils
import com.opensource.svgaplayer.utils.log.LogUtils

/**
 * Created by PonyCui on 2017/3/29.
 */
class SVGAImageView constructor(
    context: Context,
    attrs: AttributeSet?
) : ImageView(context, attrs) {

    private var tag = javaClass.simpleName


    var loops = 0

    /**
     *  结束动画后展示
     *  0   endFrame
     *  1   startFrame
     *  2   clear
     */
    var endAnimShow: Int = 1

    private var mAnimator: ValueAnimator? = null
    private var mItemClickAreaListener: SVGAClickAreaListener? = null

    //首帧
    private var mStartFrame = 0

    //尾帧
    private var mEndFrame = 0

    //是否翻转
    private var flip = false

    private var onStart: (() -> Unit)? = null
    private var onEnd: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onRepeat: (() -> Unit)? = null
    private var onPause: (() -> Unit)? = null
    private var onResume: (() -> Unit)? = null
    private var onFrame: ((frame: Int, percentage: Double) -> Unit)? = null

    constructor(context: Context) : this(context, null)

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        attrs?.let { loadAttrs(it) }

        flipView()
    }

    private fun loadAttrs(attrs: AttributeSet) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SVGAImageView, 0, 0)
        loops = typedArray.getInt(R.styleable.SVGAImageView_loopCount, 0)
        tag = typedArray.getString(R.styleable.SVGAImageView_svg_tag) ?: javaClass.simpleName
        flip = typedArray.getBoolean(R.styleable.SVGAImageView_svg_flip, false)
        endAnimShow = typedArray.getInteger(R.styleable.SVGAImageView_endAnimShow, 1)
        typedArray.recycle()
    }

    fun startAnimation() {
        startAnimation(null, false)
    }

    fun startAnimation(range: SVGARange?, reverse: Boolean = false) {
        play(range, reverse)
    }

    private fun play(range: SVGARange?, reverse: Boolean) {
        val drawable = getSVGADrawable() ?: return
        setupDrawable()

        //获取首帧
        mStartFrame = Math.max(0, range?.location ?: 0)
        val videoItem = drawable.videoItem

        //获取尾帧
        mEndFrame = Math.min(
            videoItem.frames - 1,
            ((range?.location ?: 0) + (range?.length ?: Int.MAX_VALUE) - 1)
        )

        //开启动画监听前清理一遍
        mAnimator?.cancel()
        mAnimator?.removeAllListeners()
        mAnimator?.removeAllUpdateListeners()

        val animator = ValueAnimator.ofInt(mStartFrame, mEndFrame)
        animator.interpolator = LinearInterpolator()
        animator.duration =
            ((mEndFrame - mStartFrame + 1) * (1000 / videoItem.FPS) / generateScale()).toLong()
        animator.repeatCount = if (loops <= 0) 99999 else loops - 1
        animator.addUpdateListener {
            onAnimatorUpdate(it)
//            LogUtils.error(TAG, "当前帧数-》" + (it.animatedValue as Int).toString())
        }
        animator.addListener(onStart = {
            onStart?.invoke()
            LogUtils.error(tag, "SVGA动画--》onStart")
        }, onEnd = {
            getSVGADrawable()?.let {
                when (endAnimShow) {
                    0 -> {
                        drawable.currentFrame = mEndFrame
                    }

                    1 -> {
                        drawable.currentFrame = mStartFrame
                    }

                    2 -> {
                        drawable.cleared = true
                    }
                }
            }
            onEnd?.invoke()
            stopAnim()
            LogUtils.error(tag, "SVGA动画--》onEnd")
        }, onCancel = {
            onCancel?.invoke()
            LogUtils.error(tag, "SVGA动画--》onCancel")
        }, onRepeat = {
            onRepeat?.invoke()
            LogUtils.error(tag, "SVGA动画--》onRepeat")
        })
        animator.addPauseListener(onPause = {
            onPause?.invoke()
            LogUtils.error(tag, "SVGA动画--》onPause")
        }, onResume = {
            onResume?.invoke()
            LogUtils.error(tag, "SVGA动画--》onResume")
        })
        if (reverse) {
            animator.reverse()
        } else {
            animator.start()
        }
        mAnimator = animator

        //Coil加载非match_parent控件，会回调GenericViewTarget的onSuccess
        visibilityChanged(visibility)
    }

    private fun setupDrawable() {
        val drawable = getSVGADrawable() ?: return
        drawable.cleared = false
        drawable.scaleType = scaleType
        drawable.flip = flip
    }

    private fun getSVGADrawable(): SVGADrawable? {
        return drawable as? SVGADrawable
    }

    private fun generateScale(): Double {
        var scale = 1.0
        try {
            val animatorClass = Class.forName("android.animation.ValueAnimator") ?: return scale
            val getMethod = animatorClass.getDeclaredMethod("getDurationScale") ?: return scale
            scale = (getMethod.invoke(animatorClass) as Float).toDouble()
            if (scale == 0.0) {
                val setMethod =
                    animatorClass.getDeclaredMethod("setDurationScale", Float::class.java)
                        ?: return scale
                setMethod.isAccessible = true
                setMethod.invoke(animatorClass, 1.0f)
                scale = 1.0
            }
        } catch (ignore: Exception) {
            ignore.printStackTrace()
        }
        return scale
    }

    private fun onAnimatorUpdate(animator: ValueAnimator?) {
        val drawable = getSVGADrawable() ?: return
        drawable.currentFrame = animator?.animatedValue as Int
        val percentage =
            (drawable.currentFrame + 1).toDouble() / drawable.videoItem.frames.toDouble()
        onFrame?.invoke(drawable.currentFrame, percentage)
    }

    fun clear() {
        mAnimator?.cancel()
        mAnimator?.removeAllListeners()
        mAnimator?.removeAllUpdateListeners()

        mItemClickAreaListener = null

        getSVGADrawable()?.cleared = true
        getSVGADrawable()?.clear()
        setImageDrawable(null)

        LogUtils.error(tag, "SVGA动画--》执行销毁")
    }

    fun stepToFrame(frame: Int, andPlay: Boolean) {
        pauseAnim()
        val drawable = getSVGADrawable() ?: return
        drawable.currentFrame = frame
        if (andPlay) {
            startAnimation()
            mAnimator?.let {
                it.currentPlayTime = (Math.max(
                    0.0f,
                    Math.min(1.0f, (frame.toFloat() / drawable.videoItem.frames.toFloat()))
                ) * it.duration).toLong()
            }
        }
    }

    fun stepToFrame(frame: Int, andPlay: Boolean, dynamicEntity: SVGADynamicEntity) {
        pauseAnim()
        val drawable = getSVGADrawable() ?: return
        drawable.currentFrame = frame
        drawable.setDynamicItem(dynamicEntity)

        if (andPlay) {
            startAnimation()
            mAnimator?.let {
                it.currentPlayTime = (Math.max(
                    0.0f,
                    Math.min(1.0f, (frame.toFloat() / drawable.videoItem.frames.toFloat()))
                ) * it.duration).toLong()
            }
        }
    }

    fun stepToPercentage(percentage: Double, andPlay: Boolean) {
        val drawable = drawable as? SVGADrawable ?: return
        var frame = (drawable.videoItem.frames * percentage).toInt()
        if (frame >= drawable.videoItem.frames && frame > 0) {
            frame = drawable.videoItem.frames - 1
        }
        stepToFrame(frame, andPlay)
    }

    fun setOnAnimKeyClickListener(clickListener: SVGAClickAreaListener) {
        mItemClickAreaListener = clickListener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event)
        }
        val drawable = getSVGADrawable() ?: return super.onTouchEvent(event)
        for ((key, value) in drawable.getDynamicItem().mClickMap) {
            if (event.x >= value[0] && event.x <= value[2] && event.y >= value[1] && event.y <= value[3]) {
                mItemClickAreaListener?.let {
                    it.onClick(key)
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
        clear()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        visibilityChanged(visibility)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (changedView == this) {
            visibilityChanged(visibility)
        }
    }

    fun onAnimStart(onStart: (() -> Unit)): SVGAImageView {
        this.onStart = onStart
        return this
    }

    fun onAnimPause(onPause: (() -> Unit)): SVGAImageView {
        this.onPause = onPause
        return this
    }

    fun onAnimEnd(onEnd: (() -> Unit)): SVGAImageView {
        this.onEnd = onEnd
        return this
    }

    fun onAnimRepeat(onRepeat: (() -> Unit)): SVGAImageView {
        this.onRepeat = onRepeat
        return this
    }

    fun onAnimCancel(onCancel: () -> Unit): SVGAImageView {
        this.onCancel = onCancel
        return this
    }

    fun onAnimResume(onResume: () -> Unit): SVGAImageView {
        this.onResume = onResume
        return this
    }

    fun onAnimFrame(onFrame: ((frame: Int, percentage: Double) -> Unit)): SVGAImageView {
        this.onFrame = onFrame
        return this
    }

    fun pauseAnim() {
        mAnimator?.pause()
    }

    fun resumeAnim() {
        mAnimator?.resume()
    }

    fun stopAnim() {
        getSVGADrawable()?.stop()

        LogUtils.error(tag, "SVGA动画--》执行停止")
    }

    private fun visibilityChanged(visibility: Int) {
        if (visibility == View.VISIBLE && getVisibility() == View.VISIBLE) {
            //可见
            resumeAnim()
        } else {
            //不可见
            pauseAnim()
        }
    }

    private fun flipView() {
        if (ViewUtils.isLayoutRtl() && flip) {
            scaleX = -1f
        }
    }

    fun setSvgTag(tag: String): SVGAImageView {
        this.tag = tag
        return this
    }

    /**
     * 设置是否支持水平翻转
     */
    fun setSvgFlip(flip: Boolean): SVGAImageView {
        this.flip = flip
        flipView()
        return this
    }
}