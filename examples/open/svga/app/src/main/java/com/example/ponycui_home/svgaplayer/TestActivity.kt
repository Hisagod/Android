package com.example.ponycui_home.svgaplayer

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.onSvgaAnimationFrame
import com.opensource.svgaplayer.utils.getSVGADrawable

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.iv.load("https://res.naadi.microparty.com/user/1684826637456.svga")
//        binding.iv.load("file:///android_asset/test7.svga")
        binding.iv.load("file:///android_asset/test2_text_user.svga")
    }

    fun resume(view: View) {
        binding.iv.getSVGADrawable()?.stepToFrame(30)
        val de = SVGADynamicEntity()
        de.setDynamicText("hahahaha", TextPaint().apply {
            textSize = 20f
        }, "text")
//        de.setDynamicText("hahahaha", TextPaint(), "user")
        binding.iv.getSVGADrawable()?.updateSVGADynamicEntity(de)
    }
}