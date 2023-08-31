package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import coil.load
import coil.request.CachePolicy
import com.example.ponycui_home.svgaplayer.databinding.ActivityAssetsBinding
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromAssetsBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

class AnimationFromAssetsActivity : AppCompatActivity() {
    private var currentIndex = 0

    private lateinit var binding: ActivityFromAssetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromAssetsBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val dynamic = SVGADynamicEntity()
//        val tp = TextPaint()
//        tp.textSize = 30f
//        tp.color = Color.WHITE
//        dynamic.setDynamicText("1536006", tp, "text")
//        dynamic.setDynamicImage(
//            "https://res.naadi.microparty.com/user/1673946183827.jpeg",
//            "user"
//        )
//        svg.setOnClickListener {
//            svg.stepToFrame(currentIndex++, false)
//        }

//        binding.svg.load("file:///android_asset/huolong.svga") {
//            target(SVGATarget(binding.svg) {
//                it.startAnimation()
//            })
//        }


//        binding.svg.load("file:///android_asset/mp3_to_long.svga") {
//            target(SVGATarget(binding.svg) {
//                it.startAnimation()
//            })
//        }

//        binding.iv.load("file:///android_asset/test3.png")
//        binding.iv.load("file:///android_asset/test4.gif")
//        binding.iv.load("file:///android_asset/test5.webp")

        binding.show.setOnClickListener {
            binding.svg.visibility = View.VISIBLE
        }
    }
}