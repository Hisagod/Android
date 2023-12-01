package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import coil.load

class AnimationGifActivity : AppCompatActivity() {

    private val iv by lazy { AppCompatImageView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(iv)

        iv.load("file:///android_asset/test4.gif") {
            lifecycle(this@AnimationGifActivity)
            target(ImageTarget(iv))
        }
    }
}