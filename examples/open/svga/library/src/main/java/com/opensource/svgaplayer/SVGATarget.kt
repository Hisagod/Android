package com.opensource.svgaplayer

import android.graphics.drawable.Drawable
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

//    override fun onSuccess(result: Drawable) {
//        super.onSuccess(result)
//
//        var drawable: Drawable?
//        if (result is CrossfadeDrawable) {
//            drawable = result.end
//        } else {
//            drawable = result
//        }
//
//        drawable.let {
//            val svgaDrawable = (it as SVGADrawable)
//            svgaDrawable.setDynamicItem(dynamic)
//            svg.setImageDrawable(svgaDrawable)
//            svgaDrawable.videoItem.setupAudios {
//                onSuccess.invoke(svg)
//            }
//        }
//    }

    override fun onError(error: Drawable?) {
        super.onError(error)
        LogUtils.error(TAG, "加载出错")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onResume")

//        val result = drawable
//        var drawable: Drawable?
//        if (result is CrossfadeDrawable) {
//            drawable = result.end
//        } else {
//            drawable = result
//        }
//
//        drawable.let {
//            val svgaDrawable = (it as SVGADrawable)
//            svgaDrawable.setDynamicItem(dynamic)
//            svg.setImageDrawable(svgaDrawable)
//            svgaDrawable.videoItem.setupAudios {
//                onSuccess.invoke(svg)
//            }
//        }

//        com.blankj.utilcode.util.LogUtils.e(drawable)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        LogUtils.error(TAG, "${javaClass.simpleName}->onDestroy")
    }
}