package com.example.ponycui_home.svgaplayer

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LifecycleOwner
import coil.target.ImageViewTarget
import com.blankj.utilcode.util.LogUtils

class ImageTarget(view: AppCompatImageView) : ImageViewTarget(view) {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        LogUtils.e("onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        LogUtils.e("onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        LogUtils.e("onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        LogUtils.e("onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        LogUtils.e("onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        LogUtils.e("onDestroy")
    }

    override fun onError(error: Drawable?) {
        super.onError(error)
        LogUtils.e("onError")
    }
}