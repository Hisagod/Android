package com.opensource.svgaplayer.drawer

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.core.graphics.scale
import coil.ImageLoader
import coil.request.Options
import coil.size.Scale
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.utils.contentFormat

/**
 * Created by cuiminghui on 2017/3/29.
 */

internal class SVGACanvasDrawer(videoItem: SVGAVideoEntity) :
    SGVADrawer(videoItem) {
    private val sharedValues = ShareValues()
    private val drawTextCache: ArrayMap<String, Bitmap> = arrayMapOf()
    private val pathCache = PathCache()
    private var beginIndexList: Array<Boolean>? = null
    private var endIndexList: Array<Boolean>? = null
    private var _dynamicItem: SVGADynamicEntity? = null
    private var scaleSize = 1f
    private var textSize = 0f

    override fun drawFrame(
        canvas: Canvas,
        frameIndex: Int,
        scaleType: Scale
    ) {
        super.drawFrame(canvas, frameIndex, scaleType)
        this.pathCache.onSizeChanged(canvas)
        val sprites = requestFrameSprites(frameIndex)
        // Filter null sprites
        if (sprites.count() <= 0) return
        val matteSprites = mutableMapOf<String, SVGADrawerSprite>()
        var saveID = -1
        beginIndexList = null
        endIndexList = null

        // Filter no matte layer
        var hasMatteLayer = false
        sprites.get(0).imageKey?.let {
            if (it.endsWith(".matte")) {
                hasMatteLayer = true
            }
        }
        sprites.forEachIndexed { index, svgaDrawerSprite ->

            // Save matte sprite
            svgaDrawerSprite.imageKey?.let {
                /// No matte layer included or VERSION Unsopport matte
                if (!hasMatteLayer || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // Normal sprite
                    drawSprite(svgaDrawerSprite, canvas, frameIndex)
                    // Continue
                    return@forEachIndexed
                }
                /// Cache matte sprite
                if (it.endsWith(".matte")) {
                    matteSprites.put(it, svgaDrawerSprite)
                    // Continue
                    return@forEachIndexed
                }
            }
            /// Is matte begin
            if (isMatteBegin(index, sprites)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    saveID = canvas.saveLayer(
                        0f,
                        0f,
                        canvas.width.toFloat(),
                        canvas.height.toFloat(),
                        null
                    )
                } else {
                    canvas.save()
                }
            }
            /// Normal matte
            drawSprite(svgaDrawerSprite, canvas, frameIndex)

            /// Is matte end
            if (isMatteEnd(index, sprites)) {
                matteSprites.get(svgaDrawerSprite.matteKey)?.let {
                    drawSprite(
                        it,
                        this.sharedValues.shareMatteCanvas(canvas.width, canvas.height),
                        frameIndex
                    )
                    canvas.drawBitmap(
                        this.sharedValues.sharedMatteBitmap(),
                        0f,
                        0f,
                        this.sharedValues.shareMattePaint()
                    )
                    if (saveID != -1) {
                        canvas.restoreToCount(saveID)
                    } else {
                        canvas.restore()
                    }
                    // Continue
                    return@forEachIndexed
                }
            }
        }
        releaseFrameSprites(sprites)
    }

    private fun isMatteBegin(spriteIndex: Int, sprites: List<SVGADrawerSprite>): Boolean {
        if (beginIndexList == null) {
            val boolArray = Array(sprites.count()) { false }
            sprites.forEachIndexed { index, svgaDrawerSprite ->
                svgaDrawerSprite.imageKey?.let {
                    /// Filter matte sprite
                    if (it.endsWith(".matte")) {
                        // Continue
                        return@forEachIndexed
                    }
                }
                svgaDrawerSprite.matteKey?.let {
                    if (it.length > 0) {
                        sprites.get(index - 1)?.let { lastSprite ->
                            if (lastSprite.matteKey.isNullOrEmpty()) {
                                boolArray[index] = true
                            } else {
                                if (lastSprite.matteKey != svgaDrawerSprite.matteKey) {
                                    boolArray[index] = true
                                }
                            }
                        }
                    }
                }
            }
            beginIndexList = boolArray
        }
        return beginIndexList?.get(spriteIndex) ?: false
    }

    private fun isMatteEnd(spriteIndex: Int, sprites: List<SVGADrawerSprite>): Boolean {
        if (endIndexList == null) {
            val boolArray = Array(sprites.count()) { false }
            sprites.forEachIndexed { index, svgaDrawerSprite ->
                svgaDrawerSprite.imageKey?.let {
                    /// Filter matte sprite
                    if (it.endsWith(".matte")) {
                        // Continue
                        return@forEachIndexed
                    }
                }
                svgaDrawerSprite.matteKey?.let {
                    if (it.length > 0) {
                        // Last one
                        if (index == sprites.count() - 1) {
                            boolArray[index] = true
                        } else {
                            sprites.get(index + 1)?.let { nextSprite ->
                                if (nextSprite.matteKey.isNullOrEmpty()) {
                                    boolArray[index] = true
                                } else {
                                    if (nextSprite.matteKey != svgaDrawerSprite.matteKey) {
                                        boolArray[index] = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
            endIndexList = boolArray
        }
        return endIndexList?.get(spriteIndex) ?: false
    }

    private fun shareFrameMatrix(transform: Matrix): Matrix {
        val matrix = this.sharedValues.sharedMatrix()
        matrix.postScale(scaleInfo.scaleFx, scaleInfo.scaleFy)
        matrix.postTranslate(scaleInfo.tranFx, scaleInfo.tranFy)
        matrix.preConcat(transform)
        return matrix
    }

    private fun drawSprite(sprite: SVGADrawerSprite, canvas: Canvas, frameIndex: Int) {
        drawImage(sprite, canvas)
        drawShape(sprite, canvas)
        drawDynamic(sprite, canvas, frameIndex)
    }

    private fun drawImage(sprite: SVGADrawerSprite, canvas: Canvas) {
        val imageKey = sprite.imageKey ?: return
        val isHidden = _dynamicItem?.dynamicHidden?.get(imageKey) == true
        if (isHidden) {
            return
        }
        val bitmapKey = if (imageKey.endsWith(".matte")) imageKey.substring(
            0,
            imageKey.length - 6
        ) else imageKey
//        val drawingBitmap = if (_dynamicItem?.dynamicImage?.keys?.contains(imageKey) == true) {
//            val image = videoItem.imageMap[bitmapKey]
//            image?.let {
//                val imageBitmap =
//                    _dynamicItem?.dynamicImage?.get(bitmapKey)
//                val scaleBitmap = imageBitmap?.scale(it.width, it.height, true)
//                imageBitmap?.recycle()
//                scaleBitmap
//            }
//        } else {
//            videoItem.imageMap[bitmapKey]
//        } ?: return
        val drawingBitmap =
            (_dynamicItem?.dynamicImage?.get(bitmapKey) ?: videoItem.imageMap[bitmapKey])
                ?: return
        val frameMatrix = shareFrameMatrix(sprite.frameEntity.transform)
        val paint = this.sharedValues.sharedPaint()
        paint.isAntiAlias = videoItem.antiAlias
        paint.isFilterBitmap = videoItem.antiAlias
        paint.alpha = (sprite.frameEntity.alpha * 255).toInt()
        if (sprite.frameEntity.maskPath != null) {
            val maskPath = sprite.frameEntity.maskPath ?: return
            canvas.save()
            val path = this.sharedValues.sharedPath()
            maskPath.buildPath(path)
            path.transform(frameMatrix)
            canvas.clipPath(path)
            frameMatrix.preScale(
                (sprite.frameEntity.layout.width / drawingBitmap.width).toFloat(),
                (sprite.frameEntity.layout.height / drawingBitmap.height).toFloat()
            )
            if (!drawingBitmap.isRecycled) {
                canvas.drawBitmap(drawingBitmap, frameMatrix, paint)
            }
            canvas.restore()
        } else {
            frameMatrix.preScale(
                (sprite.frameEntity.layout.width / drawingBitmap.width).toFloat(),
                (sprite.frameEntity.layout.height / drawingBitmap.height).toFloat()
            )
            if (!drawingBitmap.isRecycled) {
                canvas.drawBitmap(drawingBitmap, frameMatrix, paint)
            }
        }
        _dynamicItem?.dynamicIClickArea?.let {
            it.get(imageKey)?.let { listener ->
                val matrixArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                frameMatrix.getValues(matrixArray)
                listener.onResponseArea(
                    imageKey,
                    matrixArray[2].toInt(),
                    matrixArray[5].toInt(),
                    (drawingBitmap.width * matrixArray[0] + matrixArray[2]).toInt(),
                    (drawingBitmap.height * matrixArray[4] + matrixArray[5]).toInt()
                )
            }
        }
        drawTextOnBitmap(canvas, drawingBitmap, sprite, frameMatrix)
    }

    private fun drawTextOnBitmap(
        canvas: Canvas,
        drawingBitmap: Bitmap,
        sprite: SVGADrawerSprite,
        frameMatrix: Matrix
    ) {
        val imageKey = sprite.imageKey ?: return
        var textBitmap: Bitmap? = null
        _dynamicItem?.dynamicStaticLayoutText?.get(imageKey)?.let {
            drawTextCache[imageKey]?.let {
                textBitmap = it
            } ?: kotlin.run {
                it.paint.isAntiAlias = true
                if (it.paint.textSize != textSize) {
                    textSize = it.paint.textSize * scaleSize
                    it.paint.textSize = textSize
                }

                val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var lineMax = try {
                        val field =
                            StaticLayout::class.java.getDeclaredField("mMaximumVisibleLineCount")
                        field.isAccessible = true
                        field.getInt(it)
                    } catch (e: Exception) {
                        Int.MAX_VALUE
                    }
                    StaticLayout.Builder
                        .obtain(
                            it.text.contentFormat(),
                            0,
                            it.text.length,
                            it.paint,
                            drawingBitmap.width
                        )
                        .setAlignment(it.alignment)
                        .setMaxLines(lineMax)
                        .setEllipsize(TextUtils.TruncateAt.END)
                        .build()
                } else {
                    StaticLayout(
                        it.text.contentFormat(),
                        0,
                        it.text.length,
                        it.paint,
                        drawingBitmap.width,
                        it.alignment,
                        it.spacingMultiplier,
                        it.spacingAdd,
                        false
                    )
                }

                textBitmap = Bitmap.createBitmap(
                    drawingBitmap.width,
                    drawingBitmap.height,
                    Bitmap.Config.ARGB_8888
                )
                textBitmap?.let { b ->
                    val textCanvas = Canvas(b)
                    textCanvas.translate(
                        0f,
                        ((drawingBitmap.height - staticLayout.height) / 2).toFloat()
                    )
                    staticLayout.draw(textCanvas)
                }
                drawTextCache.put(imageKey, textBitmap as Bitmap)
            }
        }
        textBitmap?.let { textBitmap ->
            val paint = this.sharedValues.sharedPaint()
            paint.isAntiAlias = videoItem.antiAlias
            paint.alpha = (sprite.frameEntity.alpha * 255).toInt()
            if (sprite.frameEntity.maskPath != null) {
                val maskPath = sprite.frameEntity.maskPath ?: return@let
                canvas.save()
                canvas.concat(frameMatrix)
                canvas.clipRect(0, 0, drawingBitmap.width, drawingBitmap.height)
                val bitmapShader =
                    BitmapShader(textBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                paint.shader = bitmapShader
                val path = this.sharedValues.sharedPath()
                maskPath.buildPath(path)
                canvas.drawPath(path, paint)
                canvas.restore()
            } else {
                paint.isFilterBitmap = videoItem.antiAlias
                if (videoItem.textFlip) {
                    val matrix = Matrix()
                    matrix.postScale(-1f, 1f)
                    val mirrorBitmap =
                        Bitmap.createBitmap(
                            textBitmap,
                            0,
                            0,
                            textBitmap.width,
                            textBitmap.height,
                            matrix,
                            true
                        )
                    canvas.drawBitmap(mirrorBitmap, frameMatrix, paint)
                } else {
                    canvas.drawBitmap(textBitmap, frameMatrix, paint)
                }
            }
        }
    }

    private fun drawShape(sprite: SVGADrawerSprite, canvas: Canvas) {
        val frameMatrix = shareFrameMatrix(sprite.frameEntity.transform)
        sprite.frameEntity.shapes.forEach { shape ->
            shape.buildPath()
            shape.shapePath?.let {
                val paint = this.sharedValues.sharedPaint()
                paint.reset()
                paint.isAntiAlias = videoItem.antiAlias
                paint.alpha = (sprite.frameEntity.alpha * 255).toInt()
                val path = this.sharedValues.sharedPath()
                path.reset()
                path.addPath(this.pathCache.buildPath(shape))
                val shapeMatrix = this.sharedValues.sharedMatrix2()
                shapeMatrix.reset()
                shape.transform?.let {
                    shapeMatrix.postConcat(it)
                }
                shapeMatrix.postConcat(frameMatrix)
                path.transform(shapeMatrix)
                shape.styles?.fill?.let {
                    if (it != 0x00000000) {
                        paint.style = Paint.Style.FILL
                        paint.color = it
                        val alpha =
                            Math.min(255, Math.max(0, (sprite.frameEntity.alpha * 255).toInt()))
                        if (alpha != 255) {
                            paint.alpha = alpha
                        }
                        if (sprite.frameEntity.maskPath !== null) canvas.save()
                        sprite.frameEntity.maskPath?.let { maskPath ->
                            val path2 = this.sharedValues.sharedPath2()
                            maskPath.buildPath(path2)
                            path2.transform(frameMatrix)
                            canvas.clipPath(path2)
                        }
                        canvas.drawPath(path, paint)
                        if (sprite.frameEntity.maskPath !== null) canvas.restore()
                    }
                }
                shape.styles?.strokeWidth?.let {
                    if (it > 0) {
                        paint.alpha = (sprite.frameEntity.alpha * 255).toInt()
                        paint.style = Paint.Style.STROKE
                        shape.styles?.stroke?.let {
                            paint.color = it
                            val alpha =
                                Math.min(255, Math.max(0, (sprite.frameEntity.alpha * 255).toInt()))
                            if (alpha != 255) {
                                paint.alpha = alpha
                            }
                        }
                        val scale = matrixScale(frameMatrix)
                        shape.styles?.strokeWidth?.let {
                            paint.strokeWidth = it * scale
                        }
                        shape.styles?.lineCap?.let {
                            when {
                                it.equals("butt", true) -> paint.strokeCap = Paint.Cap.BUTT
                                it.equals("round", true) -> paint.strokeCap = Paint.Cap.ROUND
                                it.equals("square", true) -> paint.strokeCap = Paint.Cap.SQUARE
                            }
                        }
                        shape.styles?.lineJoin?.let {
                            when {
                                it.equals("miter", true) -> paint.strokeJoin = Paint.Join.MITER
                                it.equals("round", true) -> paint.strokeJoin = Paint.Join.ROUND
                                it.equals("bevel", true) -> paint.strokeJoin = Paint.Join.BEVEL
                            }
                        }
                        shape.styles?.miterLimit?.let {
                            paint.strokeMiter = it.toFloat() * scale
                        }
                        shape.styles?.lineDash?.let {
                            if (it.size == 3 && (it[0] > 0 || it[1] > 0)) {
                                paint.pathEffect = DashPathEffect(
                                    floatArrayOf(
                                        (if (it[0] < 1.0f) 1.0f else it[0]) * scale,
                                        (if (it[1] < 0.1f) 0.1f else it[1]) * scale
                                    ), it[2] * scale
                                )
                            }
                        }
                        if (sprite.frameEntity.maskPath !== null) canvas.save()
                        sprite.frameEntity.maskPath?.let { maskPath ->
                            val path2 = this.sharedValues.sharedPath2()
                            maskPath.buildPath(path2)
                            path2.transform(frameMatrix)
                            canvas.clipPath(path2)
                        }
                        canvas.drawPath(path, paint)
                        if (sprite.frameEntity.maskPath !== null) canvas.restore()
                    }
                }
            }

        }
    }

    private val matrixScaleTempValues = FloatArray(16)

    private fun matrixScale(matrix: Matrix): Float {
        matrix.getValues(matrixScaleTempValues)
        if (matrixScaleTempValues[0] == 0f) {
            return 0f
        }
        var A = matrixScaleTempValues[0].toDouble()
        var B = matrixScaleTempValues[3].toDouble()
        var C = matrixScaleTempValues[1].toDouble()
        var D = matrixScaleTempValues[4].toDouble()
        if (A * D == B * C) return 0f
        var scaleX = Math.sqrt(A * A + B * B)
        A /= scaleX
        B /= scaleX
        var skew = A * C + B * D
        C -= A * skew
        D -= B * skew
        var scaleY = Math.sqrt(C * C + D * D)
        C /= scaleY
        D /= scaleY
        skew /= scaleY
        if (A * D < B * C) {
            scaleX = -scaleX
        }
        return if (scaleInfo.ratioX) Math.abs(scaleX.toFloat()) else Math.abs(scaleY.toFloat())
    }

    private fun drawDynamic(sprite: SVGADrawerSprite, canvas: Canvas, frameIndex: Int) {
        val imageKey = sprite.imageKey ?: return
        _dynamicItem?.dynamicDrawer?.get(imageKey)?.let {
            val frameMatrix = shareFrameMatrix(sprite.frameEntity.transform)
            canvas.save()
            canvas.concat(frameMatrix)
            it.invoke(canvas, frameIndex)
            canvas.restore()
        }
        _dynamicItem?.dynamicDrawerSized?.get(imageKey)?.let {
            val frameMatrix = shareFrameMatrix(sprite.frameEntity.transform)
            canvas.save()
            canvas.concat(frameMatrix)
            it.invoke(
                canvas,
                frameIndex,
                sprite.frameEntity.layout.width.toInt(),
                sprite.frameEntity.layout.height.toInt()
            )
            canvas.restore()
        }
    }

    fun setSVGADynamicEntity(de: SVGADynamicEntity?) {
        this._dynamicItem = de
    }

    fun getSVGADynamicEntity() = _dynamicItem

    fun setScaleSize(scaleSize: Float) {
        this.scaleSize = scaleSize
    }
}
