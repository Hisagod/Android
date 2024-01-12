package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.entities.SVGARtlEntity
import com.opensource.svgaplayer.utils.getSVGADrawable
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaRtl

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.iv.load("https://res.naadi.microparty.com/user/1684826637456.svga")
        binding.iv.load("file:///android_asset/test.svga") {
//        binding.iv.load("file:///android_asset/test7.svga") {
//        binding.iv.scaleX = -1f
//        val de = SVGADynamicEntity()
//        de.setDynamicText("مرحبا 123؟", TextPaint().apply {
//            textSize = 25f
//        }, "text")

//        de.setDynamicText("123456", TextPaint().apply {
//            textSize = 25f
//        }, "text")
//        binding.iv.load("file:///android_asset/test2_text_user.svga") {
//            svgaRtl(SVGARtlEntity(binding.iv))
//            svgaDynamicEntity(de)
        }
    }

    fun resume(view: View) {
        binding.iv.getSVGADrawable()?.stepToFrame(30)
        val de = SVGADynamicEntity()
//        de.setDynamicText("مرحبا 123؟", TextPaint().apply {
//            textSize = 25f
//        }, "text")

        de.setDynamicText("更换了元素", TextPaint().apply {
            textSize = 25f
        }, "text")

//        de.setDynamicText("hahahaha", TextPaint(), "user")
        binding.iv.getSVGADrawable()?.updateSVGADynamicEntity(de)
    }

    fun flip(view: View) {

    }
}