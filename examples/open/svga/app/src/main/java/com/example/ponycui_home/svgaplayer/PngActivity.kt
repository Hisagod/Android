package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityPngBinding
import okio.ByteString.Companion.encodeUtf8

class PngActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPngBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPngBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun local(view: View) {
        binding.iv.load("file:///android_asset/test3.png") {
            target(ImageTarget(binding.iv))
        }
    }

    fun net(view: View) {
        binding.iv.load("https://res.naadi.microparty.com/user/1683720175906.png")
    }
}