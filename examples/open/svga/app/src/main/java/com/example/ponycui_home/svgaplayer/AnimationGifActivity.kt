package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import coil.load
import coil.request.CachePolicy
import com.example.ponycui_home.svgaplayer.databinding.ActivityAssetsBinding
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromAssetsBinding
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

class AnimationGifActivity : AppCompatActivity() {

    private val iv by lazy { AppCompatImageView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(iv)

        iv.load("file:///android_asset/test4.gif")
    }
}