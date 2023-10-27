package com.opensource.svgaplayer.drawer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

class ShareValues {
    private val sharedPaint = Paint()
    private val sharedPath = Path()
    private val sharedPath2 = Path()
    private val sharedMatrix = Matrix()
    private val sharedMatrix2 = Matrix()

    private val shareMattePaint = Paint()
    private var shareMatteCanvas: Canvas? = null
    private var sharedMatteBitmap: Bitmap? = null

    fun sharedPaint(): Paint {
        sharedPaint.reset()
        return sharedPaint
    }

    fun sharedPath(): Path {
        sharedPath.reset()
        return sharedPath
    }

    fun sharedPath2(): Path {
        sharedPath2.reset()
        return sharedPath2
    }

    fun sharedMatrix(): Matrix {
        sharedMatrix.reset()
        return sharedMatrix
    }

    fun sharedMatrix2(): Matrix {
        sharedMatrix2.reset()
        return sharedMatrix2
    }

    fun shareMattePaint(): Paint {
        shareMattePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        return shareMattePaint
    }

    fun sharedMatteBitmap(): Bitmap {
        return sharedMatteBitmap as Bitmap
    }

    fun shareMatteCanvas(width: Int, height: Int): Canvas {
        if (shareMatteCanvas == null) {
            sharedMatteBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
//                shareMatteCanvas = Canvas(sharedMatteBitmap)
        }
//            val matteCanvas = shareMatteCanvas as Canvas
//            matteCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
//            return matteCanvas
        return sharedMatteBitmap?.let {
            Canvas(it)
        } ?: Canvas()
    }

    fun onClear() {
        shareMatteCanvas = null

        sharedMatteBitmap?.recycle()
        sharedMatteBitmap = null
    }
}