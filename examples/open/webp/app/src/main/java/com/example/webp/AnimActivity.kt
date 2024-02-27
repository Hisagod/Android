package com.example.webp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.webp.databinding.ActivityAnimBinding

class AnimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.iv.load(R.drawable.pic_home_activity)
//        binding.iv.load(R.raw.play_one)
//        binding.iv.load(R.raw.test5)
        binding.iv.load(R.raw.test6)
    }
}