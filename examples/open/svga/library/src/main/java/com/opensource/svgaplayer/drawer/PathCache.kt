package com.opensource.svgaplayer.drawer

import android.graphics.Canvas
import android.graphics.Path
import com.opensource.svgaplayer.entities.SVGAVideoShapeEntity

class PathCache {
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private val cache = HashMap<SVGAVideoShapeEntity, Path>()

    fun onSizeChanged(canvas: Canvas) {
        if (this.canvasWidth != canvas.width || this.canvasHeight != canvas.height) {
            this.cache.clear()
        }
        this.canvasWidth = canvas.width
        this.canvasHeight = canvas.height
    }

    fun buildPath(shape: SVGAVideoShapeEntity): Path {
        if (!this.cache.containsKey(shape)) {
            val path = Path()
            shape.shapePath?.let {
                path.set(it)
            }
            this.cache[shape] = path
        }
        return this.cache[shape]!!
    }
}
