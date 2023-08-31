package com.example.ponycui_home.svgaplayer.fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.FragmentDBinding

class DFragment : BaseLazyFragment<FragmentDBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_d

    override fun initData() {
        binding.svg.visibility = View.VISIBLE
    }
}