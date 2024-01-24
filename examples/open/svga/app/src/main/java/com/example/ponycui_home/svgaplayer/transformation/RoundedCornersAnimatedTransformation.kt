package com.example.ponycui_home.svgaplayer.transformation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import coil.transform.AnimatedTransformation
import coil.transform.PixelOpacity

class RoundedCornersAnimatedTransformation : AnimatedTransformation {

    override fun transform(canvas: Canvas): PixelOpacity {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        val path = Path().apply {
            fillType = Path.FillType.INVERSE_EVEN_ODD
        }

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()
        path.addRoundRect(40f, 40f, width, height, 40f, 40f, Path.Direction.CW)
        canvas.drawPath(path, paint)
        return PixelOpacity.TRANSLUCENT
    }
}