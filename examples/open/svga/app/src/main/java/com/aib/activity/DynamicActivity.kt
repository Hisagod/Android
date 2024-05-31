package com.aib.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.blankj.utilcode.util.ConvertUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.ActivityDynamicBinding
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromFragmentBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.svgaDynamicEntity

class DynamicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDynamicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDynamicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entity = SVGADynamicEntity()
        entity.setDynamicImage(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
            "user"
        )
        val text = "12345678910"
        val pain = TextPaint()
        pain.textSize = ConvertUtils.sp2px(11f).toFloat()
        val staticLayout = StaticLayout.Builder
            .obtain(text, 0, text.length, pain, 350)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()
        entity.setDynamicText(staticLayout, "text")
        binding.svg.load(R.raw.test2_text_user) {
            svgaDynamicEntity(entity)
        }
    }
}