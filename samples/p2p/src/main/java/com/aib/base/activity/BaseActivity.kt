package com.aib.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aib.base.IPage
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils

abstract class BaseActivity<D : ViewDataBinding> : AppCompatActivity(), IPage {
    lateinit var binding: D
    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
        logPageName()
        binding = DataBindingUtil.setContentView(this, getLayoutId())

        initData()
    }

    override fun logPageName() {
        LogUtils.e("打开：${javaClass.simpleName}")
    }
}