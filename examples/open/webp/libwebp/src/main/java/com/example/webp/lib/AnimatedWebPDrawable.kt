package com.example.webp.lib

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.ImageLoader
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException

class AnimatedWebPDrawable(
    private val decoder: LibWebPAnimatedDecoder,
    private val name: String,
    private val imageLoader: ImageLoader
) : Drawable(), Animatable2Compat {
    private val TAG = javaClass.simpleName

    private val superScope by lazy { imageLoader.defaults.decoderDispatcher }

    private val paint by lazy(LazyThreadSafetyMode.NONE) { Paint(Paint.FILTER_BITMAP_FLAG) }
    private val decodeChannel by lazy { Channel<LibWebPAnimatedDecoder.DecodeFrameResult>() }
    private var decodeJob: Job? = null
    private var frameWaitingJob: Job? = null
    private var pendingDecodeResult: LibWebPAnimatedDecoder.DecodeFrameResult? = null
    private var nextFrame = false
    private var isRunning = false
    private val callbacks = mutableListOf<Animatable2Compat.AnimationCallback>()
    private var currentDecodingResult: LibWebPAnimatedDecoder.DecodeFrameResult? = null
    private var queueTime = -1L
    private var queueDelay = INITIAL_QUEUE_DELAY_HEURISTIC
    private var queueDelayWindow = ArrayDeque(listOf(INITIAL_QUEUE_DELAY_HEURISTIC))
    private var queueDelaySum = INITIAL_QUEUE_DELAY_HEURISTIC

    private val nextFrameScheduler = {
        nextFrame = true
        queueTime = SystemClock.uptimeMillis()
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val time = SystemClock.uptimeMillis()
        if (queueTime >= 0) {
            val currentDelay = time - queueTime
            addQueueDelay(currentDelay)
            queueTime = -1
        }

        val channel = decodeChannel
        if (!isRunning || !nextFrame || channel == null) {
            currentDecodingResult?.bitmap?.let {
                canvas.drawBitmap(it, null, bounds, paint)
            }
            return
        }

        nextFrame = false

        val decodeFrameResult = pendingDecodeResult?.also {
            pendingDecodeResult = null
        } ?: channel.tryReceive().getOrNull()

        if (decodeFrameResult == null) {
            currentDecodingResult?.bitmap?.let {
                canvas.drawBitmap(it, null, bounds, paint)
            }
            if (decodeJob?.isActive != true && channel.isEmpty) {
                stop()
            } else if (frameWaitingJob?.isActive != true) {
                frameWaitingJob =
                    CoroutineScope(superScope).launch(Dispatchers.Main.immediate) {
                        try {
                            pendingDecodeResult = channel.receive()
                            nextFrame = true
                            queueTime = SystemClock.uptimeMillis()
                            invalidateSelf()
                        } catch (e: ClosedReceiveChannelException) {
                            // failed to receive next frame
                        } finally {
                            frameWaitingJob = null
                        }
                    }
            }
        } else {
            canvas.drawBitmap(decodeFrameResult.bitmap, null, bounds, paint)
            currentDecodingResult = decodeFrameResult
            if (decodeJob?.isActive != true && channel.isEmpty) {
                stop()
            } else {
                scheduleSelf(
                    nextFrameScheduler,
                    time + (decodeFrameResult.frameLengthMs - queueDelay).coerceAtLeast(0)
                )
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return decoder.width
    }

    override fun getIntrinsicHeight(): Int {
        return decoder.height
    }

    override fun start() {
        Logger.e(TAG, "动画开始")
        if (isRunning) return
        isRunning = true

        callbacks.forEach { it.onAnimationStart(this) }
        nextFrame = true
        invalidateSelf()

        decodeJob =
            CoroutineScope(superScope).launch(Dispatchers.Default) {
                val loopCount = decoder.loopCount
                var i = 0
                while (isActive && (loopCount == 0 || i < loopCount)) {
                    decoder.reset()
                    for (frame in 0 until decoder.frameCount) {
                        val result = decoder.decodeNextFrame(frame, name, imageLoader)

                        if (!isActive) {
                            break
                        }

                        if (result == null) {
                            continue
                        }

                        try {
                            decodeChannel.send(result)
                        } catch (e: ClosedSendChannelException) {
                            break
                        }
                    }
                    ++i
                }
            }
    }

    /**
     * 单次播放动画结束回调
     */
    override fun stop() {
        Logger.e(TAG, "动画停止")
        if (!isRunning) return
        isRunning = false

        decodeJob?.cancel()
        decodeJob = null

        decodeChannel.close()
//        decodeChannel = null

        frameWaitingJob?.cancel()
        frameWaitingJob = null

        nextFrame = false
        unscheduleSelf(nextFrameScheduler)

        callbacks.forEach { it.onAnimationEnd(this) }
    }

    override fun isRunning(): Boolean {
        return isRunning
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

    private fun addQueueDelay(delay: Long) {
        val coercedDelay = delay.coerceAtMost(MAX_QUEUE_DELAY_HEURISTIC)
        queueDelayWindow.addLast(coercedDelay)
        queueDelaySum += coercedDelay
        while (queueDelayWindow.size > QUEUE_DELAY_WINDOW_COUNT) {
            queueDelaySum -= queueDelayWindow.removeFirst()
        }
        queueDelay = (queueDelaySum / queueDelayWindow.size).coerceAtMost(MAX_QUEUE_DELAY_HEURISTIC)
    }

    companion object {
        private const val INITIAL_QUEUE_DELAY_HEURISTIC = 11L
        private const val MAX_QUEUE_DELAY_HEURISTIC = 21L
        private const val QUEUE_DELAY_WINDOW_COUNT = 20
    }
}