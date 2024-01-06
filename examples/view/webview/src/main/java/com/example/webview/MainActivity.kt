package com.example.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.MessengerUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MessengerUtils.register()
//        EventBus.getDefault().register(this)

        MessengerUtils.subscribe("fromWeb") {
            val name = it.getString("name")
            LogUtils.e(name)

            val bundle = Bundle()
            bundle.putString("name", "我来自主进程")
            MessengerUtils.post("fromMain", bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MessengerUtils.unregister()
//        EventBus.getDefault().unregister(this)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    fun onEvent(code: Int) {
//        if (code == 200) {
//
//        }
//    }

    fun enter(view: View) {
        ActivityUtils.startActivity(WebActivity::class.java)
    }

    fun send(view: View) {

    }
}