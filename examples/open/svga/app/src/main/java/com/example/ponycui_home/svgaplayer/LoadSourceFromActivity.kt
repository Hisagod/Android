package com.example.ponycui_home.svgaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityLoadSourceFromBinding
import com.opensource.svgaplayer.utils.svgaRepeatCount

/**
 * 展示加载源文件方式
 */
class LoadSourceFromActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadSourceFromBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadSourceFromBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //asserts文件夹加载
//        binding.iv1.load("file:///android_asset/test7.svga")
        binding.iv1.load("file:///android_asset/copter.svga")
        //网络加载
        binding.iv2.load("https://res.naadi.microparty.com/user/1684826637456.svga")
    }
}