package com.opensource.svgaplayer

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import coil.drawable.CrossfadeDrawable
import coil.target.ImageViewTarget
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGATarget(
    private val svg: SVGAImageView,
    private val dynamic: SVGADynamicEntity = SVGADynamicEntity(),
    private val onSuccess: (iv: SVGAImageView) -> Unit
) : ImageViewTarget(svg) {
    private val TAG = "SVGATarget"

    private val observer = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            if (svg.visibility == View.VISIBLE) {
                svg.resumeAnim()
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            svg.pauseAnim()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            svg.stopAnim()
        }
    }

    init {
        try {
            svg.findFragment<Fragment>().lifecycle.addObserver(observer)
        } catch (e: Exception) {
            LogUtils.error(TAG, "没找到Fragment")
            if (svg.context is AppCompatActivity) {
                (svg.context as AppCompatActivity).lifecycle.addObserver(observer)
            } else {
                throw Exception("请传入AppCompatActivity的上下文")
            }
        }
    }

    override fun onSuccess(result: Drawable) {
        super.onSuccess(result)

        var drawable: Drawable?
        if (result is CrossfadeDrawable) {
            drawable = result.end
        } else {
            drawable = result
        }

        drawable.let {
            val svgaDrawable = (it as SVGADrawable)
            svgaDrawable.setDynamicItem(dynamic)
            svg.setImageDrawable(svgaDrawable)
            svgaDrawable.videoItem.setupAudios {
                onSuccess.invoke(svg)
            }
        }
    }

    override fun onError(error: Drawable?) {
        super.onError(error)
        LogUtils.error(TAG, "加载出错")
    }
}