package com.example.ponycui_home.svgaplayer

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.opensource.svgaplayer.SVGATarget

object ImageViewBinding {
    @JvmStatic
    @BindingAdapter("loadSVGA")
    fun loadSVGA(view: SVGAImageView, url: Any?) {
        view.load("file:///android_asset/${url}") {
            target(SVGATarget(view) {
                it.startAnimation()
            })
        }
    }

    @JvmStatic
    @BindingAdapter("loadImg")
    fun loadImg(view: AppCompatImageView, url: Any?) {
        view.load(url)
    }
}