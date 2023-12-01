package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

class AnimationFromJavaCodeActivity : AppCompatActivity() {

    private val svg by lazy { AppCompatImageView(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(svg)
        svg.background = ColorDrawable(Color.BLACK)

        svg.load("file:///android_asset/test7.svga") {
            target(ImageTarget(svg))
        }

        svg.setOnClickListener {
            startActivity(Intent(this, PngActivity::class.java))
        }
    }
}