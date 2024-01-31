package com.opensource.svgaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 * 相对于AppCompatImageView，新增元素点击事件
 */
open class SVGAImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var click: ((clickKey: String) -> Unit)? = null

    private fun getSVGADrawable(): SVGADrawable? {
        return drawable as? SVGADrawable
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event)
        }
        val drawable = getSVGADrawable() ?: return super.onTouchEvent(event)
        val entity = drawable.getSVGADynamicEntity() ?: return super.onTouchEvent(event)
        for ((key, value) in entity.mClickMap) {
            if (event.x >= value[0] && event.x <= value[2] && event.y >= value[1] && event.y <= value[3]) {
                click?.invoke(key)
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    fun onItemClick(click: (clickKey: String) -> Unit) {
        this.click = click
    }
}