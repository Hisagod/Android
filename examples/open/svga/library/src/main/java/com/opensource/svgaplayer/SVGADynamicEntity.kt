package com.opensource.svgaplayer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.BoringLayout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.collection.ArrayMap
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by cuiminghui on 2017/3/30.
 */
class SVGADynamicEntity {

    internal var dynamicHidden: ArrayMap<String, Boolean> = ArrayMap()

    internal var dynamicImage: ArrayMap<String, Bitmap> = ArrayMap()

    internal var dynamicText: ArrayMap<String, String> = ArrayMap()

    internal var dynamicTextPaint: ArrayMap<String, TextPaint> = ArrayMap()

    internal var dynamicStaticLayoutText: ArrayMap<String, StaticLayout> = ArrayMap()

    internal var dynamicBoringLayoutText: ArrayMap<String, BoringLayout> = ArrayMap()

    internal var dynamicDrawer: ArrayMap<String, (canvas: Canvas, frameIndex: Int) -> Boolean> =
        ArrayMap()

    //点击事件回调map
    internal var mClickMap: ArrayMap<String, IntArray> = ArrayMap()
    internal var dynamicIClickArea: ArrayMap<String, IClickAreaListener> = ArrayMap()

    internal var dynamicDrawerSized: ArrayMap<String, (canvas: Canvas, frameIndex: Int, width: Int, height: Int) -> Boolean> =
        ArrayMap()


    internal var isTextDirty = false

    fun setHidden(value: Boolean, forKey: String) {
        this.dynamicHidden.put(forKey, value)
    }

    fun setDynamicImage(bitmap: Bitmap, forKey: String) {
        this.dynamicImage.put(forKey, bitmap)
    }

    fun setDynamicText(text: String, textPaint: TextPaint, forKey: String) {
        this.isTextDirty = true
        this.dynamicText.put(forKey, text)
        this.dynamicTextPaint.put(forKey, textPaint)
    }

    fun setDynamicText(layoutText: StaticLayout, forKey: String) {
        this.isTextDirty = true
        this.dynamicStaticLayoutText.put(forKey, layoutText)
    }

    fun setDynamicText(layoutText: BoringLayout, forKey: String) {
        this.isTextDirty = true
        BoringLayout.isBoring(layoutText.text, layoutText.paint)?.let {
            this.dynamicBoringLayoutText.put(forKey, layoutText)
        }
    }

    fun setDynamicDrawer(drawer: (canvas: Canvas, frameIndex: Int) -> Boolean, forKey: String) {
        this.dynamicDrawer.put(forKey, drawer)
    }

    fun setClickArea(clickKey: List<String>) {
        for (itemKey in clickKey) {
            dynamicIClickArea.put(itemKey, object : IClickAreaListener {
                override fun onResponseArea(key: String, x0: Int, y0: Int, x1: Int, y1: Int) {
                    mClickMap.let {
                        if (it.get(key) == null) {
                            it.put(key, intArrayOf(x0, y0, x1, y1))
                        } else {
                            it.get(key)?.let {
                                it[0] = x0
                                it[1] = y0
                                it[2] = x1
                                it[3] = y1
                            }
                        }
                    }
                }
            })
        }
    }

    fun setClickArea(clickKey: String) {
        dynamicIClickArea.put(clickKey, object : IClickAreaListener {
            override fun onResponseArea(key: String, x0: Int, y0: Int, x1: Int, y1: Int) {
                mClickMap.let {
                    if (it.get(key) == null) {
                        it.put(key, intArrayOf(x0, y0, x1, y1))
                    } else {
                        it.get(key)?.let {
                            it[0] = x0
                            it[1] = y0
                            it[2] = x1
                            it[3] = y1
                        }
                    }
                }
            }
        })
    }

    fun setDynamicDrawerSized(
        drawer: (canvas: Canvas, frameIndex: Int, width: Int, height: Int) -> Boolean,
        forKey: String
    ) {
        this.dynamicDrawerSized.put(forKey, drawer)
    }
}