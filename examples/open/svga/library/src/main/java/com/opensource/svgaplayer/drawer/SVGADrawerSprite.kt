package com.opensource.svgaplayer.drawer

import com.opensource.svgaplayer.entities.SVGAVideoSpriteFrameEntity

class SVGADrawerSprite(
    var _matteKey: String? = null,
    var _imageKey: String? = null,
    var _frameEntity: SVGAVideoSpriteFrameEntity? = null
) {
    val matteKey get() = _matteKey
    val imageKey get() = _imageKey
    val frameEntity get() = _frameEntity!!
}