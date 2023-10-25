package com.opensource.svgaplayer.ext

import androidx.core.text.BidiFormatter
import androidx.core.text.TextDirectionHeuristicsCompat
import java.util.Locale

/**
 *阿语和英语混排适配
 */
fun CharSequence.contentFormat(): CharSequence {
    return BidiFormatter.getInstance(Locale.getDefault())
        .unicodeWrap(this, TextDirectionHeuristicsCompat.LOCALE)
}