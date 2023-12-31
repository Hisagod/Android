package com.example.aidl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.example.aidl.databinding.ActivityRoomBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomBinding
    private val testBean = TestBean("1")

    override fun onCreate(savedInstanceState: Bundle?) {
        RemoteServiceBinder.openService(this)
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.receive.setOnClickListener {

        }

        binding.send.setOnClickListener {
            RemoteServiceBinder.request("我来自Client")
        }

        binding.testReturn.setOnClickListener {
            RemoteServiceBinder.testReturn(testBean) {
                LogUtils.e(testBean.toString())
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onShowMsg(msg: String) {
        LogUtils.e(msg)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        RemoteServiceBinder.closeService(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onServiceDeadEvent(event: ServiceDeadEvent) {
        RemoteServiceBinder.openService(this)
        EventBus.getDefault().removeStickyEvent(event)
    }
}
