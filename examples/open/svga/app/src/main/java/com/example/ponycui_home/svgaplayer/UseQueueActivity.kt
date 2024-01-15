package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.iterator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityUseQueueBinding
import com.opensource.svgaplayer.utils.svgaAnimationEnd
import com.opensource.svgaplayer.utils.svgaRepeatCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.resume

/**
 * 结合队列依次播放动画
 */
class UseQueueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUseQueueBinding
    private val queue = LinkedBlockingQueue<Int>()
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUseQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun add(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (job == null || job?.isActive == false) {
                    suspendCancellableCoroutine<Unit> {
                        runQueue()

                        if (job != null) {
                            if (it.isActive) {
                                it.resume(Unit)
                            }
                        }
                    }
                }

                queue.put(1)
            }
        }
    }

    fun stop(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                queue.put(0)
            }
        }
    }

    private fun runQueue() {
        job = lifecycleScope.launch(Dispatchers.Default) {
            while (true) {
                val num = withContext(Dispatchers.IO) {
                    queue.take()
                }

                if (num == 0) {
                    LogUtils.e("取消")
                    cancel()
                }

                withContext(Dispatchers.Main) {
                    suspendCancellableCoroutine<Unit> {
                        val svga = AppCompatImageView(this@UseQueueActivity)
                        svga.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        svga.load("file:///android_asset/tomato.svga") {
                            svgaRepeatCount(1)
                            svgaAnimationEnd {
                                val iterator = binding.ll.iterator()
                                while (iterator.hasNext()) {
                                    val next = iterator.next()
                                    if (next is AppCompatImageView) {
                                        iterator.remove()
                                    }
                                }

                                if (it.isActive) {
                                    it.resume(Unit)
                                }
                            }
                        }
                        binding.ll.addView(svga)
                    }
                }
            }
        }
    }
}