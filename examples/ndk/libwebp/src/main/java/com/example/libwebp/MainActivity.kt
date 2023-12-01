package com.example.libwebp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.WebpBitmapFactory
import com.bumptech.glide.integration.webp.decoder.WebpDownsampler
import com.example.libwebp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    init {
        System.loadLibrary("webp")
        System.loadLibrary("webpdecoder")
        System.loadLibrary("webpdemux")
        System.loadLibrary("webpmux")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.gif.load(R.drawable.gif)
//        binding.webp.load(R.drawable.webp)

//        Glide.with(this).load(R.drawable.gif).into(binding.gif)
//        Glide.with(this).load(R.drawable.webp).into(binding.webp)

        loadImage(binding.gif, R.drawable.gif)
        loadImage(binding.webp, R.drawable.webp)
    }

    private fun loadImage(imageView: ImageView, url: Any) {
        WebpBitmapFactory.sUseSystemDecoder = false
        Glide.with(this)
            .load(url)
            .set(WebpDownsampler.USE_SYSTEM_DECODER, false)
            .into(imageView)
    }
}