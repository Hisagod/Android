package com.example.ponycui_home.svgaplayer.fragment

import androidx.databinding.ViewDataBinding
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.FragmentABinding
import com.opensource.svgaplayer.utils.svgaAnimationFrame

class AFragment : BaseLazyFragment<FragmentABinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_a

    override fun initData() {
        binding.svg.load("file:///android_asset/test.svga"){
            svgaAnimationFrame {
//                LogUtils.e("A")
            }
        }
    }
}