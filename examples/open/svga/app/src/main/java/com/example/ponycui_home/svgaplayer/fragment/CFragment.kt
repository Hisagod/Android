package com.example.ponycui_home.svgaplayer.fragment

import androidx.databinding.ViewDataBinding
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.FragmentCBinding
import com.opensource.svgaplayer.utils.svgaAnimationFrame

class CFragment : BaseLazyFragment<FragmentCBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_c

    override fun initData() {
        binding.svg.load("file:///android_asset/copter.svga"){
            svgaAnimationFrame {
//                LogUtils.e("C")
            }
        }
    }
}