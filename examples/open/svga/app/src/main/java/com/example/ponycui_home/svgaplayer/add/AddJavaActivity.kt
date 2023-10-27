package com.example.ponycui_home.svgaplayer.add

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityAddJavaBinding
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

/**
 * 动态添加SVGA控件，并播放动画
 */
class AddJavaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJavaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJavaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val svga = SVGAImageView(this)
        svga.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        svga.load("file:///android_asset/test7.svga") {
            target(SVGATarget(svga) {
                it.startAnimation()
            })
        }
        binding.ll.addView(svga)
    }

    fun remove(view: View) {
        val iterator = binding.ll.iterator()
        while (iterator.hasNext()) {
            val nextView = iterator.next()
            if (nextView is SVGAImageView) {
                iterator.remove()
            }
        }
    }
}