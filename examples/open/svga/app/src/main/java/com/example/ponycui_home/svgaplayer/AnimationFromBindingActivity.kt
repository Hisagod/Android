package com.example.ponycui_home.svgaplayer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

class AnimationFromBindingActivity : AppCompatActivity() {

    private val svg by lazy { SVGAImageView(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(svg)
        svg.background = ColorDrawable(Color.BLACK)


        val dynamic = SVGADynamicEntity()
        val tp = TextPaint()
        tp.textSize = 30f
        tp.color = Color.WHITE
        dynamic.setDynamicText("1536006", tp, "text")
        dynamic.setDynamicImage(
            "https://res.naadi.microparty.com/user/1673946183827.jpeg",
            "user"
        )

        svg.load("file:///android_asset/huolong.svga") {
            target(SVGATarget(svg) {
                it.startAnimation()
            })
        }
    }
}