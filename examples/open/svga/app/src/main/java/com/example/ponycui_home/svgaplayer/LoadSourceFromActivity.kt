package com.example.ponycui_home.svgaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityLoadSourceFromBinding

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
        binding.iv1.load("file:///android_asset/test7.svga")
        //网络加载
        binding.iv2.load("https://github.com/yyued/SVGA-Samples/blob/master/posche.svga?raw=true")
    }
}