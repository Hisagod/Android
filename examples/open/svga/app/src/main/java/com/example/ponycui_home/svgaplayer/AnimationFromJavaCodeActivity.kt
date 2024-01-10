package com.example.ponycui_home.svgaplayer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGATarget
import com.opensource.svgaplayer.onSvgaAnimationEnd
import com.opensource.svgaplayer.onSvgaAnimationFrame
import com.opensource.svgaplayer.onSvgaAnimationStart

class AnimationFromJavaCodeActivity : AppCompatActivity() {

    private val svg by lazy { AppCompatImageView(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(svg)
        svg.background = ColorDrawable(Color.BLACK)

//        svg.scaleType = ImageView.ScaleType.CENTER_INSIDE

        //加载SVGA
//        val de = SVGADynamicEntity()
//        de.setDynamicText("测试文本", TextPaint().apply {
//            color = Color.BLACK
//        }, "text")
        svg.load("file:///android_asset/test.svga") {
//            svgaRepeatCount(1)
            onSvgaAnimationStart {
                LogUtils.e("开始动画")
            }
            onSvgaAnimationEnd {
                LogUtils.e("停止动画")
            }
            onSvgaAnimationFrame {
//                LogUtils.e("当前帧数：${it}")
            }
//            target(SVGATarget(svg, de))
        }

        //加载gif
//        svg.load("file:///android_asset/test4.gif"){
//            target {
//                svg.setImageDrawable(it)
//            }
//        }
    }
}