package com.aib.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import com.example.ponycui_home.svgaplayer.R

class GifActivity : AppCompatActivity() {

    private val iv by lazy { AppCompatImageView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(iv)

        iv.load(R.raw.test4)
    }
}