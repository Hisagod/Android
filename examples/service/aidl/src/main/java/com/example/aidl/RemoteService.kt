package com.example.aidl

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock


class RemoteService : LifecycleService() {

    private val lock = ReentrantLock()
    private val mCallBacks = RemoteCallbackList<ICallback>()

    private val iService = object : ISender.Stub() {
        override fun registerCallback(cb: ICallback) {
            mCallBacks.register(cb)
        }

        override fun unRegisterCallback(cb: ICallback) {
            mCallBacks.unregister(cb)
        }

        override fun onClientRequest(bean: SenderBean<*>?) {
            bean?.let {
                when (it.request) {
                    SenderConstant.ACTION_ASYNC_RETURN -> {
                        LogUtils.e("S端收到")
                        runCallbackOnMain {
                            it.showLog("异步，回调")
                        }
                    }
                }
            }
        }

        override fun test(bean: TestBean) {
            bean.str = "9999"
        }
    }

    override fun onBind(p0: Intent): IBinder {
        super.onBind(p0)
        return iService
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createNotification()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        LogUtils.e(this.javaClass.simpleName + ":onTaskRemoved")

        val activityManager =
            App.instance.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val pids = activityManager?.runningAppProcesses?.map { it.pid } ?: emptyList()
        pids.forEach { Process.killProcess(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        //销毁资源
        mCallBacks.kill()

        //关闭服务
        finishService()

        LogUtils.e(this.javaClass.simpleName + ":onDestroy")
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = AppUtils.getAppPackageName()
            val notificationChannel =
                NotificationChannel(
                    channelId,
                    AppUtils.getAppName(),
                    NotificationManager.IMPORTANCE_LOW
                )
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 80, 40)
            NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)

            val notification = NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .build()
            startForeground(1, notification)
        }
    }

    private fun finishService() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    /**
     * 使用同步锁保证正常执行
     */
    private fun runCallbackOnMain(onItem: (obj: ICallback) -> Unit) {
        lock.lock()
        try {
            for (item in 0 until mCallBacks.beginBroadcast()) {
                try {
                    onItem.invoke(mCallBacks.getBroadcastItem(item))
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            mCallBacks.finishBroadcast()
        } catch (e: Exception) {
            Log.e("HLP", e.message ?: "")
        } finally {

        }
        lock.unlock()
    }
}