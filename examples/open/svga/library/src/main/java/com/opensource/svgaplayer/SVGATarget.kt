package com.opensource.svgaplayer

import android.graphics.drawable.Drawable
import coil.drawable.CrossfadeDrawable
import coil.target.ImageViewTarget
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGATarget(
    private val svg: SVGAImageView,
    private val dynamic: SVGADynamicEntity = SVGADynamicEntity(),
    private val onSuccess: (iv: SVGAImageView) -> Unit
) : ImageViewTarget(svg) {
    private val TAG = "SVGATarget"

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