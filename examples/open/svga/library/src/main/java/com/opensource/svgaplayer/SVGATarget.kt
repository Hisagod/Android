package com.opensource.svgaplayer

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import coil.drawable.CrossfadeDrawable
import coil.target.ImageViewTarget
import com.opensource.svgaplayer.utils.log.LogUtils

class SVGATarget(
    private val svg: ImageView,
    private val de: SVGADynamicEntity
) : ImageViewTarget(svg) {
    private val TAG = "SVGATarget"

    override fun onSuccess(result: Drawable) {
        super.onSuccess(result)
        LogUtils.error(TAG, "${TAG}->加载成功")

        var drawable: Drawable?
        if (result is CrossfadeDrawable) {
            drawable = result.end
        } else {
            drawable = result
        }

        (drawable as? SVGADrawable)?.let {
            it.updateSVGADynamicEntity(de)
        }
    }

    override fun onError(error: Drawable?) {
        super.onError(error)
        LogUtils.error(TAG, "${TAG}->加载出错")
    }
}