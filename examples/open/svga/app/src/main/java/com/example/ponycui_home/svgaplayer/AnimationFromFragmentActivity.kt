package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import coil.load
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromFragmentBinding
import com.example.ponycui_home.svgaplayer.databinding.FragmentABinding
import com.opensource.svgaplayer.utils.svgaAnimationFrame

class AnimationFromFragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.vp.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 5

            override fun createFragment(position: Int): Fragment = AFragment(position)
        }
    }

    class AFragment(val position: Int) : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return FragmentABinding.inflate(inflater).root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val svg = view.findViewById<AppCompatImageView>(R.id.svg)
            svg.load("file:///android_asset/copter.svga")
        }
    }
}