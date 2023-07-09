package com.aib.base.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aib.base.IPage
import com.aib.p2p.R
import com.aib.p2p.databinding.ViewToolbarWithContentBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils

abstract class BaseToolbarActivity<D : ViewDataBinding> : AppCompatActivity(), IPage {
    lateinit var binding: D
    lateinit var rootBinding: ViewToolbarWithContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
        logPageName()
        rootBinding = DataBindingUtil.inflate(layoutInflater, R.layout.view_toolbar_with_content, findViewById<FrameLayout>(android.R.id.content), true)
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), rootBinding.flContent, true)

        setTitle()

        initData()

        rootBinding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun logPageName() {
        LogUtils.e("打开：${javaClass.simpleName}")
    }
}