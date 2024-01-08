package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromNetworkBinding

class AnimationFromNetworkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromNetworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.svg.load("https://res.naadi.microparty.com/user/1684826637456.svga") {
//            target(SVGATarget(binding.svg) {
//                it.startAnimation()
//            })
//        }
    }
}