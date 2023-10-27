package com.example.ponycui_home.svgaplayer.load

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromAssetsBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGATarget

class LoadFromAssetsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromAssetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromAssetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dynamic = SVGADynamicEntity()
        val tp = TextPaint()
        tp.textSize = 20f
        tp.color = Color.WHITE
        dynamic.setDynamicText("1536006", tp, "text")
        dynamic.setDynamicImage(
            "https://res.naadi.microparty.com/user/1673946183827.jpeg",
            "user"
        )

        binding.svg.load("file:///android_asset/test2_text_user.svga") {
            target(SVGATarget(binding.svg, dynamic) {
                it.startAnimation()
            })
        }
    }
}