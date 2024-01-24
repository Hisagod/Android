package com.example.ponycui_home.svgaplayer.transformation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import coil.transform.AnimatedTransformation
import coil.transform.PixelOpacity

class AnimatedCircleTransformation : AnimatedTransformation {
    override fun transform(canvas: Canvas): PixelOpacity {
        val path = Path()
        path.fillType = Path.FillType.INVERSE_EVEN_ODD
        val width = canvas.width
        val height = canvas.height
        val radius = width / 2f
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        canvas.drawPath(path, paint)
        return PixelOpacity.TRANSLUCENT
    }
}