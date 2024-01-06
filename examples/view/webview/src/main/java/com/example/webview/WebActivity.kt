package com.example.webview

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.MessengerUtils
import com.example.webview.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindService(Intent(this, MainService::class.java), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }, BIND_AUTO_CREATE)


//        MessengerUtils.register()

        MessengerUtils.subscribe("fromMain") {
            val name = it.getString("name")
            LogUtils.e(name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MessengerUtils.unregister()
    }

    fun send(view: View) {
        val bundle = Bundle()
        bundle.putString("name", "我来自Web进程")
        MessengerUtils.post("fromWeb", bundle)
    }
}