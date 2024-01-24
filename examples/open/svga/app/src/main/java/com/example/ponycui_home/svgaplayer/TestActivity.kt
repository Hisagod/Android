package com.example.ponycui_home.svgaplayer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Paint
import coil.load
import coil.request.animatedTransformation
import coil.transform.Transformation
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.example.ponycui_home.svgaplayer.databinding.ActivityTestBinding
import com.example.ponycui_home.svgaplayer.transformation.AnimatedCircleTransformation
import com.example.ponycui_home.svgaplayer.transformation.MirrorTransform
import com.example.ponycui_home.svgaplayer.transformation.RoundedCornersAnimatedTransformation
import com.example.ponycui_home.svgaplayer.transformation.ScaleTransform
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.utils.getSVGADrawable
import com.opensource.svgaplayer.utils.svgaAnimationFrame
import com.opensource.svgaplayer.utils.svgaDynamicEntity
import com.opensource.svgaplayer.utils.svgaScale

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}