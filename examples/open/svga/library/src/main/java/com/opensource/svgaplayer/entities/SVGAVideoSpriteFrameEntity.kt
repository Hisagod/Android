package com.opensource.svgaplayer.entities

import android.graphics.Matrix
import com.opensource.svgaplayer.factory.SVGARectFactory
import com.opensource.svgaplayer.proto.FrameEntity

/**
 * Created by cuiminghui on 2016/10/17.
 */
class SVGAVideoSpriteFrameEntity {

    var alpha: Double
    var layout = SVGARect(0.0, 0.0)
    var transform = Matrix()
    var maskPath: SVGAPathEntity? = null
    var shapes: MutableList<SVGAVideoShapeEntity> = mutableListOf()

    constructor(obj: FrameEntity) {
        this.alpha = (obj.alpha ?: 0.0f).toDouble()
        obj.layout?.let {
            this.layout = SVGARect(it.width.toDouble(), it.height.toDouble())
        }
        obj.transform?.let {
            val arr = FloatArray(9)
            val a = it.a ?: 1.0f
            val b = it.b ?: 0.0f
            val c = it.c ?: 0.0f
            val d = it.d ?: 1.0f
            val tx = it.tx ?: 0.0f
            val ty = it.ty ?: 0.0f
            arr[0] = a
            arr[1] = c
            arr[2] = tx
            arr[3] = b
            arr[4] = d
            arr[5] = ty
            arr[6] = 0.0f
            arr[7] = 0.0f
            arr[8] = 1.0f
            transform.setValues(arr)
        }
        obj.clipPath?.takeIf { it.isNotEmpty() }?.let {
            maskPath = SVGAPathEntity(it)
        }
        this.shapes = obj.shapes.map {
            return@map SVGAVideoShapeEntity(it)
        }.toMutableList()
    }
}
