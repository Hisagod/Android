package com.opensource.svgaplayer.utils

import androidx.appcompat.widget.AppCompatImageView
import com.opensource.svgaplayer.SVGADrawable

fun AppCompatImageView.getSVGADrawable(): SVGADrawable? {
    return this.drawable as? SVGADrawable
}