package com.example.ponycui_home.svgaplayer

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Size
import coil.size.ViewSizeResolver
import com.blankj.utilcode.util.LogUtils

object ImageViewBinding {
    @JvmStatic
    @BindingAdapter("loadImg")
    fun loadImg(view: AppCompatImageView, url: Any?) {
        view.load(url)
    }
}