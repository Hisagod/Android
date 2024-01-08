package com.example.ponycui_home.svgaplayer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import coil.load

class AnimationFromJavaCodeActivity : AppCompatActivity() {

    private val svg by lazy { AppCompatImageView(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(svg)
        svg.background = ColorDrawable(Color.BLACK)

//        svg.scaleType = ImageView.ScaleType.CENTER_INSIDE

        //加载SVGA
        svg.load("file:///android_asset/test7.svga") {
            target(ImageTarget(svg))
        }

        //加载gif
//        svg.load("file:///android_asset/test4.gif")
    }
}