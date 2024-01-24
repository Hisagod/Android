package com.example.ponycui_home.svgaplayer.transformation

import android.graphics.Bitmap
import androidx.core.graphics.scale
import coil.size.Size
import coil.transform.Transformation

class ScaleTransform(private val scaleSize: Float = 1f) : Transformation {
    override val cacheKey = "${javaClass.name}-$scaleSize"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        return input.scale(
            (input.width * scaleSize).toInt(),
            (input.height * scaleSize).toInt(),
            false
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is ScaleTransform &&
                scaleSize == other.scaleSize
    }

    override fun hashCode(): Int {
        return scaleSize.hashCode()
    }
}