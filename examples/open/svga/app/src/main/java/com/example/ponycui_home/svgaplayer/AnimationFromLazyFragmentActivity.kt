package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromFragmentBinding
import com.example.ponycui_home.svgaplayer.fragment.AFragment
import com.example.ponycui_home.svgaplayer.fragment.BFragment
import com.example.ponycui_home.svgaplayer.fragment.CFragment
import com.example.ponycui_home.svgaplayer.fragment.DFragment

class AnimationFromLazyFragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.vp.offscreenPageLimit = 4
        binding.vp.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 6

            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> {
                    AFragment()
                }

                1 -> {
                    BFragment()
                }

                2 -> {
                    CFragment()
                }

                3 -> {
                    DFragment()
                }

                else -> {
                    DFragment()
                }
            }
        }
    }
}