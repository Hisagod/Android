package com.aib.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.ActivityUseQueueBinding
import com.opensource.svgaplayer.utils.svgaAnimationEnd
import com.opensource.svgaplayer.utils.svgaRepeatCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * 结合队列依次播放动画
 */
class UseQueueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUseQueueBinding
    private var job: Job? = null

    private var channel = Channel<Int>(capacity = Channel.UNLIMITED)
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUseQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        channel.close()
    }

    fun start(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {
            try {
                if (job == null || job?.isActive == false) {
                    runQueue()
                }

                count = count.inc()
                channel.send(count)
                LogUtils.e("添加数据")
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }

    fun pause(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {
            job?.cancelAndJoin()
        }
    }

    fun stop(view: View) {
        lifecycleScope.launch(Dispatchers.Default) {
            channel.close()
        }
    }

    private fun runQueue() {
        job = lifecycleScope.launch(Dispatchers.Default) {
            try {
                while (this.isActive) {
                    val receive = channel.receive()
                    LogUtils.e("取出:${receive}-channel:${channel}")
                    withContext(Dispatchers.Main) {
                        suspendCancellableCoroutine<Unit> {
                            binding.iv.load(R.raw.rose) {
                                svgaRepeatCount(1)
                                svgaAnimationEnd {
                                    if (it.isActive) {
                                        it.resume(Unit)
                                    }
                                }
                            }
                        }
                    }
                }
//                channel.consumeEach {
//                    withContext(Dispatchers.Main) {
//                        suspendCancellableCoroutine<Unit> {
//                            binding.iv.load("file:///android_asset/tomato.svga") {
//                                svgaRepeatCount(1)
//                                svgaAnimationEnd {
//                                    if (it.isActive) {
//                                        it.resume(Unit)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }
}