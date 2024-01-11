package com.opensource.svgaplayer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.opensource.svgaplayer.utils.log.LogUtils
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.FileDescriptor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object SVGAAudioManager : LifecycleObserver {
    private val TAG = javaClass.simpleName
    private val soundPool by lazy { getSoundPool(20) }
    private var onFinish: ((sampleId: Int) -> Unit)? = null
    val pool = CopyOnWriteArrayList<Int>()

    /**
     * 音量设置，范围在 [0, 1] 之间
     */
    private var volume: Float = 1f

    init {
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                LogUtils.error(TAG, "音频ID：${sampleId}加载完成")
                onFinish?.invoke(sampleId)
            }
        }
    }

    fun onFinish(onFinish: (sampleId: Int) -> Unit) {
        this.onFinish = onFinish
    }

    internal fun load(
        fd: FileDescriptor?,
        offset: Long,
        length: Long,
        priority: Int
    ): Int {
        val soundId = soundPool.load(fd, offset, length, priority)
        pool.add(soundId)
        LogUtils.error(TAG, "音频ID：${soundId}")
        return soundId
    }

    internal fun unload(soundId: Int) {
        LogUtils.error(TAG, "unload soundId=$soundId")
        soundPool.unload(soundId)
    }

    internal fun play(loadId: Int): Int {
        LogUtils.error(TAG, "play loadId=$loadId")
        return soundPool.play(loadId, volume, volume, 1, 0, 1.0f)
    }

    internal fun stop(playId: Int) {
        LogUtils.error(TAG, "stop playId=$playId")
        soundPool.stop(playId)
    }

    internal fun resume(playId: Int) {
        LogUtils.error(TAG, "resume playId=$playId")
        soundPool.resume(playId)
    }

    internal fun pause(playId: Int) {
        LogUtils.error(TAG, "pause playId=$playId")
        soundPool.pause(playId)
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume(owner: LifecycleOwner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop(owner: LifecycleOwner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy(owner: LifecycleOwner) {

    }
}