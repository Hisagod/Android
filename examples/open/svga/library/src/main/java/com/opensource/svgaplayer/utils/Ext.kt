package com.opensource.svgaplayer.utils

import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.BidiFormatter
import androidx.core.text.TextDirectionHeuristicsCompat
import coil.request.ImageRequest
import coil.request.Parameters
import com.opensource.svgaplayer.SVGADecoder
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.entities.SVGARtlEntity
import java.util.Locale

/**
 * 获取SVGADrawable
 */
fun AppCompatImageView.getSVGADrawable(): SVGADrawable? {
    return drawable as? SVGADrawable
}

/**
 *阿语和英语混排适配
 */
fun CharSequence.contentFormat(): CharSequence {
    return BidiFormatter.getInstance(Locale.getDefault())
        .unicodeWrap(this, TextDirectionHeuristicsCompat.LOCALE)
}

/**
 * 是否是RTL布局
 */
fun View.isLayoutRtl(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val primaryLocale: Locale
        primaryLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
        return TextUtils.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL
    }
    return false
}

fun Parameters.svgaRepeatCount(): Int? = value(SVGADecoder.REPEAT_COUNT_KEY)

fun ImageRequest.Builder.svgaRepeatCount(repeatCount: Int): ImageRequest.Builder {
    return setParameter(SVGADecoder.REPEAT_COUNT_KEY, repeatCount)
}

fun ImageRequest.Builder.onSvgaAnimationStart(callback: (() -> Unit)?): ImageRequest.Builder {
    return setParameter(SVGADecoder.ANIMATION_START_CALLBACK_KEY, callback)
}

fun Parameters.svgaAnimationStartCallback(): (() -> Unit)? =
    value(SVGADecoder.ANIMATION_END_CALLBACK_KEY)

fun ImageRequest.Builder.onSvgaAnimationEnd(callback: (() -> Unit)?): ImageRequest.Builder {
    return setParameter(SVGADecoder.ANIMATION_END_CALLBACK_KEY, callback)
}

fun Parameters.svgaAnimationEndCallback(): (() -> Unit)? =
    value(SVGADecoder.ANIMATION_END_CALLBACK_KEY)

fun ImageRequest.Builder.onSvgaAnimationFrame(callback: ((frame: Int) -> Unit)?): ImageRequest.Builder {
    return setParameter(SVGADecoder.ANIMATION_FRAME_CALLBACK_KEY, callback)
}

fun Parameters.svgaAnimationFrameCallback(): ((frame: Int) -> Unit)? =
    value(SVGADecoder.ANIMATION_FRAME_CALLBACK_KEY)

fun Parameters.svgaRtl(): SVGARtlEntity? = value(SVGADecoder.SVGA_RTL)

fun ImageRequest.Builder.svgaRtl(entity: SVGARtlEntity): ImageRequest.Builder {
    return setParameter(SVGADecoder.SVGA_RTL, entity)
}

fun Parameters.svgaDynamicEntity(): SVGADynamicEntity? = value(SVGADecoder.SVGA_DYNAMIC)

fun ImageRequest.Builder.svgaDynamicEntity(entity: SVGADynamicEntity): ImageRequest.Builder {
    return setParameter(SVGADecoder.SVGA_DYNAMIC, entity)
}