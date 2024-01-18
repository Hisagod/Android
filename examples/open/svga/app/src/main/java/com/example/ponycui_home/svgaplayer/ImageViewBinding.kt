package com.example.ponycui_home.svgaplayer

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Size
import coil.size.ViewSizeResolver
import com.blankj.utilcode.util.LogUtils

object ImageViewBinding {
    @JvmStatic
    @BindingAdapter("loadSVGA")
    fun loadSVGA(view: AppCompatImageView, url: Any?) {
//        view.load("file:///android_asset/${url}") {
//            target(SVGATarget(view) {
//                it.startAnimation()
//            })
//        }
    }

    @JvmStatic
    @BindingAdapter("loadImg")
    fun loadImg(view: AppCompatImageView, url: Any?) {
        view.post {
            LogUtils.e("宽" + view.width)
            LogUtils.e("高" + view.height)

            val size = Math.max(view.width, view.height)
            view.load(url) {
                allowHardware(false)
//                size(size / 6)
                size(200)
            }
        }
    }
}