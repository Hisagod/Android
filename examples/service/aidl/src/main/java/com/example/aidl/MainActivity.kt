package com.example.aidl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.example.aidl.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        RemoteServiceBinder.openService(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.asyncReturn.setOnClickListener {
            RemoteServiceBinder.onClientRequest(SenderConstant.ACTION_ASYNC_RETURN)
        }

//        binding.send.setOnClickListener {
//            RemoteServiceBinder.request(SenderConstant.TEXT, "我先执行")
//        }
//
//        binding.sendObj.setOnClickListener {
//            RemoteServiceBinder.request(SenderConstant.SENDER_CUSTOM_OBJ, UserBean("客户端"))
//        }

//        binding.btn.setOnClickListener {
//            startActivity(Intent(this, RoomActivity::class.java))
//        }
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
