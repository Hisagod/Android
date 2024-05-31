package com.example.ponycui_home.svgaplayer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Paint
import androidx.lifecycle.Lifecycle
import coil.load
import coil.request.animatedTransformation
import coil.transform.Transformation
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.getSVGADrawable
import com.opensource.svgaplayer.utils.svgaAnimationFrame
import com.opensource.svgaplayer.utils.svgaAnimationStart
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaScale

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val entity = SVGADynamicEntity()
//        entity.setDynamicImage(BitmapFactory.decodeResource(resources, R.drawable.test1), "user")
//        val text = "12345678910"
//        val pain = TextPaint()
//        pain.textSize = ConvertUtils.sp2px(11f).toFloat()
//        val staticLayout = StaticLayout.Builder
//            .obtain(text, 0, text.length, pain, 350)
//            .setAlignment(Layout.Alignment.ALIGN_CENTER)
//            .build()
//        entity.setDynamicText(staticLayout, "text")
//        entity.setDynamicText(text, "text")
//        binding.iv.load("https://res.naadi.microparty.com/user/1678535055967.zip")
        binding.iv.load(R.raw.tomato)
    }
}