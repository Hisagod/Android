package com.example.ponycui_home.svgaplayer.use

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityUseQueueBinding
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget
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
                        val svga = SVGAImageView(this@UseQueueActivity)
                        svga.loops = 1
                        svga.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        svga.onAnimEnd {
                            runOnUiThread {
                                val iterator = binding.ll.iterator()
                                while (iterator.hasNext()) {
                                    val next = iterator.next()
                                    if (next is SVGAImageView) {
                                        iterator.remove()
                                    }
                                }

                                if (it.isActive) {
                                    it.resume(Unit)
                                }
                            }
                        }
                        svga.load("file:///android_asset/test7.svga") {
                            target(SVGATarget(svga) {
                                it.startAnimation()
                            })
                        }
                        binding.ll.addView(svga)
                    }
                }
            }
        }
    }
}