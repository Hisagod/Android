package com.atguigu.mobileplayer2.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aib.app.Jump
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils

class SplashActivity : ComponentActivity() {
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jump()
        }
        PermissionUtils.permission(PermissionConstants.STORAGE)
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    handler.postDelayed({ //两秒后才执行到这里
                        //执行在主线程中
                        startMainActivity()
                    }, 2000)
                }
            }.request()
    }

    private var isStartMain = false

    /**
     * 跳转到主页面，并且把当前页面关闭掉
     */
    private fun startMainActivity() {
        if (!isStartMain) {
            isStartMain = true
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            //关闭当前页面
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        startMainActivity()
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        //把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}