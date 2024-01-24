package com.example.ponycui_home.svgaplayer.transformation

import android.graphics.Bitmap
import android.graphics.Matrix
import coil.size.Size
import coil.transform.CircleCropTransformation
import coil.transform.Transformation

/**
 * 水平镜像反转，动图不进行翻转
 */
class MirrorTransform : Transformation {
    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(
            input,
            0,
            0,
            input.width,
            input.height,
            matrix,
            true
        )
    }

    override fun equals(other: Any?) = other is CircleCropTransformation

    override fun hashCode() = javaClass.hashCode()
}