package com.opensource.svgaplayer

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.opensource.svgaplayer.drawer.SVGACanvasDrawer

class SVGADrawable(val videoItem: SVGAVideoEntity) : Drawable() {

    var cleared = true
        internal set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidateSelf()
        }

    var currentFrame = 0
        internal set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidateSelf()
        }

    var scaleType: ImageView.ScaleType = ImageView.ScaleType.MATRIX

    private var drawer: SVGACanvasDrawer? = null

    override fun draw(canvas: Canvas) {
        if (cleared) {
            return
        }
        drawer?.drawFrame(canvas, currentFrame, scaleType)
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    fun resume() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.resume(it)
            }
        }
    }

    fun pause() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.pause(it)
            }
        }
    }


    fun stop() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.stop(it)
            }
        }
    }

    fun clear() {
        videoItem.audioList.forEach { audio ->
            audio.playID?.let {
                SVGASoundManager.stop(it)
            }
            audio.playID = null
        }
        videoItem.clear()

        getDynamicItem().clearDynamicObjects()
    }

    fun setDynamicItem(dynamicItem: SVGADynamicEntity) {
        drawer = SVGACanvasDrawer(videoItem, dynamicItem)
    }

    fun getDynamicItem(): SVGADynamicEntity {
        return drawer?.dynamicItem ?: SVGADynamicEntity()
    }
}