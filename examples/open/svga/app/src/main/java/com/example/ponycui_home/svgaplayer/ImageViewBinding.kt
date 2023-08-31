package com.example.ponycui_home.svgaplayer

import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Size
import coil.size.SizeResolver
import coil.size.ViewSizeResolver
import com.opensource.svgaplayer.SVGAImageView
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