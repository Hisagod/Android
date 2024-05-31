package com.aib.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.ActivityClickBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaRtl

class ClickActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClickBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dynamicEntity = SVGADynamicEntity()
        dynamicEntity.setClickArea("img_10")
        binding.iv.load(R.raw.merry_christmas) {
            svgaDynamicEntity(dynamicEntity)
        }
        binding.iv.onItemClick {
            ToastUtils.showShort("点击元素:${it}")
        }
    }
}