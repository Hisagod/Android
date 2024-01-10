package com.opensource.svgaplayer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal object SVGALifecycle : Lifecycle() {

    private val owner = object : LifecycleOwner {
        override val lifecycle get() = this@SVGALifecycle
    }

    override val currentState get() = State.RESUMED

    override fun addObserver(observer: LifecycleObserver) {
        require(observer is DefaultLifecycleObserver) {
            "$observer must implement androidx.lifecycle.DefaultLifecycleObserver."
        }

        // Call the lifecycle methods in order and do not hold a reference to the observer.
        observer.onResume(owner)
        observer.onStop(owner)
        observer.onDestroy(owner)
    }

    override fun removeObserver(observer: LifecycleObserver) {}
}