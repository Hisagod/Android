package com.opensource.svgaplayer.drawer

import android.graphics.Canvas
import android.widget.ImageView
import coil.size.Scale
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.utils.Pools
import kotlin.math.max

/**
 * Created by cuiminghui on 2017/3/29.
 */

open internal class SGVADrawer(val videoItem: SVGAVideoEntity) {

    val scaleInfo = SVGAScaleInfo()

    private val spritePool = Pools.SimplePool<SVGADrawerSprite>(max(1, videoItem.spriteList.size))

    internal fun requestFrameSprites(frameIndex: Int): List<SVGADrawerSprite> {
        return videoItem.spriteList.mapNotNull {
            if (frameIndex >= 0 && frameIndex < it.frames.size) {
                it.imageKey?.let { imageKey ->
                    if (!imageKey.endsWith(".matte") && it.frames[frameIndex].alpha <= 0.0) {
                        return@mapNotNull null
                    }
                    return@mapNotNull (spritePool.acquire() ?: SVGADrawerSprite()).apply {
                        _matteKey = it.matteKey
                        _imageKey = it.imageKey
                        _frameEntity = it.frames[frameIndex]
                    }
                }
            }
            return@mapNotNull null
        }
    }

    internal fun releaseFrameSprites(sprites: List<SVGADrawerSprite>) {
        sprites.forEach { spritePool.release(it) }
    }

    open fun drawFrame(
        canvas: Canvas,
        frameIndex: Int,
        scaleType: Scale
    ) {
        scaleInfo.performScaleType(
            canvas.width.toFloat(),
            canvas.height.toFloat(),
            videoItem.videoSize.width.toFloat(),
            videoItem.videoSize.height.toFloat(),
            scaleType
        )
    }

}
