package com.aib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils

abstract class BaseLazyFragment<D : ViewDataBinding> : Fragment() {

    lateinit var binding: D
        private set

    private var isLoad = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        LogUtils.e("onCreateView")
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!isLoad) {
            isLoad = true
            initData()

            LogUtils.e("懒加载")
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun initData()
}