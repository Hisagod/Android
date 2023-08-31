package com.opensource.svgaplayer.transform

import android.graphics.Bitmap
import android.graphics.Matrix
import coil.size.Size
import coil.transform.Transformation

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
}