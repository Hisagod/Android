package com.aib.fragment

import androidx.databinding.ViewDataBinding
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.FragmentABinding
import com.example.ponycui_home.svgaplayer.databinding.FragmentBBinding
import com.opensource.svgaplayer.utils.svgaAnimationFrame

class BFragment : BaseLazyFragment<FragmentBBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_b

    override fun initData() {

    }
}