package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.getSVGADrawable
import com.opensource.svgaplayer.utils.svgaAnimationFrame

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.iv.load("https://res.naadi.microparty.com/user/1684826637456.svga")
//        binding.iv.load("file:///android_asset/test.svga") {
//        binding.iv.load("file:///android_asset/test7.svga") {
//        binding.iv.scaleX = -1f
//        val de = SVGADynamicEntity()
//        de.setDynamicText("مرحبا 123؟", TextPaint().apply {
//            textSize = 25f
//        }, "text")

//        de.setDynamicText("123456", TextPaint().apply {
//            textSize = 25f
//        }, "text")
        binding.iv.load("file:///android_asset/ic_enter_room_loading.svga") {
        }
    }


    fun resume(view: View) {

    }

    fun flip(view: View) {

    }
}