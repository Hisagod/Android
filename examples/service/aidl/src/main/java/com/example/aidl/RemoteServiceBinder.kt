package com.example.aidl

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import kotlin.system.exitProcess

object RemoteServiceBinder {
    private var iSender: ISender? = null

    //服务是否绑定
    private var isBind = false

    private val deathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            LogUtils.e("Service：binderDied")
            try {
                //解除Binder死亡监听
                iSender?.asBinder()?.unlinkToDeath(this, 0)
                //重置
                iSender = null
                //重新绑定服务
                EventBus.getDefault().postSticky(ServiceDeadEvent())
            } catch (e: Exception) {
                Log.e("HLP", e.message ?: "")
            }
        }
    }

    private val con = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                //使用asInterface方法取得AIDL对应的操作接口
                iSender = ISender.Stub.asInterface(service)
                //设置Binder死亡监听
                iSender?.asBinder()?.linkToDeath(deathRecipient, 0)
                //把接收消息的回调接口注册到服务端
                iSender?.registerCallback(callback)

                LogUtils.e("Service：onServiceConnected")
            } catch (e: Exception) {
                Log.e("HLP", e.message ?: "")
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            try {
                LogUtils.e("Service：onServiceDisconnected")
            } catch (e: Exception) {
                Log.e("HLP", e.message ?: "")
            }
        }
    }


    private fun startService(ctx: Context, action: String? = null, bundle: Bundle? = null) {
        val intent = Intent(ctx, RemoteService::class.java).apply {
            action?.let {
                this.action = it
            }
            bundle?.let {
                putExtras(it)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(intent)
        } else {
            ctx.startService(intent)
        }
    }

    private fun bindService(ctx: Context) {
        isBind = ctx.bindService(
            Intent(ctx, RemoteService::class.java),
            con,
            Context.BIND_AUTO_CREATE
        )
    }

    fun openService(ctx: Context) {
        startService(ctx)
        bindService(ctx)
    }

    private fun unBindService(ctx: Context) {
        try {
            if (isBind) {
                ctx.unbindService(con)
            }
        } catch (e: Exception) {
            LogUtils.e(e.message ?: "")
        } finally {
            isBind = false
        }
    }

    private fun stopService(ctx: Context) {
        ctx.stopService(Intent(ctx, RemoteService::class.java))
    }

    fun closeService(ctx: Context) {
//        if (iSender?.asBinder()?.isBinderAlive == true) {
//            iSender?.unRegisterCallback(callback)
//        }
        unBindService(ctx)
        stopService(ctx)
    }

    private fun isBinderAlive(): Boolean {
        if (iSender?.asBinder()?.isBinderAlive == true) {
            return true
        }

        //重连操作
        unBindService(App.instance)

        return false
    }

    fun onClientRequest(action: String) {
        MainScope().launch {
            try {
                iSender?.onClientRequest(SenderBean(SenderConstant.ACTION_ASYNC_RETURN, null))
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }

    fun request(s: String) {
        MainScope().launch(Dispatchers.Default) {
            try {
//                iSender?.onClientRequest(SenderBean(SenderConstant.TEXT, s))
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }

    fun <D> request(action: String, params: D) {
        MainScope().launch(Dispatchers.Default) {
            try {
                iSender?.onClientRequest(SenderBean(action, params))
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }

    fun testReturn(bean: TestBean, onSuccess: () -> Unit) {
        MainScope().launch(Dispatchers.Default) {
            try {
                val result = iSender?.test(bean)

                onSuccess.invoke()
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
    }

    //服务端数据传递至绑定的本Activity
    private val callback = object : ICallback.Stub() {
        override fun showLog(msg: String) {
            MainScope().launch(Dispatchers.Default) {
                try {
                    EventBus.getDefault().postSticky(msg)
                    delay(20 * 1000)
                    LogUtils.e("结束睡眠")
                } catch (e: Exception) {
                    LogUtils.e(e.message)
                }
            }

            LogUtils.e("当前线程数：${Thread.activeCount()}")
        }
    }
}