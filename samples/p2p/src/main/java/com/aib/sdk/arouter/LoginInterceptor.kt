package com.aib.sdk.arouter

import android.content.Context
import com.aib.sdk.sp.SpManager
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Interceptor(priority = 1)
class LoginInterceptor : IInterceptor {

    private lateinit var ctx: Context

    override fun init(context: Context) {
        this.ctx = context
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LoginPoint {
        fun getSpManager(): SpManager
    }

    private fun getSpManager(ctx: Context): SpManager {
        val point = EntryPointAccessors.fromApplication(ctx, LoginPoint::class.java)
        return point.getSpManager()
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val spManager = getSpManager(ctx)
        val userId = spManager.getUserId()
        if (userId.isNullOrEmpty()) {
            when (postcard.path) {
                ArouterPath.PATH_USER_INFO -> {
                    callback.onInterrupt(Throwable("请用户登录"))
                }
                else -> callback.onContinue(postcard)
            }
        } else {
            callback.onContinue(postcard)
        }
    }
}