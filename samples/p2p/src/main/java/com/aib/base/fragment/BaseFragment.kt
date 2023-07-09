package com.aib.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.aib.base.IPage
import com.blankj.utilcode.util.LogUtils

/**
 * 不适用VP2+TL模式
 *
 * 没有懒加载模式
 */
abstract class BaseFragment<D : ViewDataBinding> : Fragment(), IPage {
    lateinit var binding: D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logPageName()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun logPageName() {
        LogUtils.e("打开：${javaClass.simpleName}")
    }
}