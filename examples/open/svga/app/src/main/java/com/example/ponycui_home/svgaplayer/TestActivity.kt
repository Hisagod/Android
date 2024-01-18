package com.example.ponycui_home.svgaplayer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Paint
import coil.load
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.getSVGADrawable
import com.opensource.svgaplayer.utils.svgaAnimationFrame
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaScale

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

//        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//        val dynamic = SVGADynamicEntity()
//        dynamic.setDynamicText("ABCDEFGHIJKLMN", TextPaint().apply {
//            textSize = 9f
//        }, "text")
//        dynamic.setDynamicImage(bitmap, "user")
//        binding.iv.load("file:///android_asset/test2_text_user.svga") {
//            svgaDynamicEntity(dynamic)
//            svgaScale(0.5f)
//        }

        ImageViewBinding.loadImg(binding.iv,R.drawable.test1)

    }


    fun resume(view: View) {

    }

    fun flip(view: View) {

    }
}