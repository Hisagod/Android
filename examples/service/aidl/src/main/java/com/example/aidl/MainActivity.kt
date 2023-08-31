package com.example.aidl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.example.aidl.databinding.ActivityMainBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        RemoteServiceBinder.openService(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.receive.setOnClickListener {

        }

        binding.send.setOnClickListener {
            RemoteServiceBinder.request("我来自Client")
        }

        binding.sendObj.setOnClickListener {
            RemoteServiceBinder.request(SenderConstant.SENDER_CUSTOM_OBJ, UserBean("客户端"))
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
