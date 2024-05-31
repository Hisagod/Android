package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aib.activity.ClickActivity
import com.aib.activity.DynamicActivity
import com.aib.activity.FragmentActivity
import com.aib.activity.GifActivity
import com.aib.activity.ListActivity
import com.aib.activity.ResourceListActivity
import com.aib.activity.UseQueueActivity
import com.example.ponycui_home.svgaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun test(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun resourceList(view: View) {
        startActivity(Intent(this, ResourceListActivity::class.java))
    }

    fun svgaClick(view: View) {
        startActivity(Intent(this, ClickActivity::class.java))
    }

    fun dynamic(view: View) {
        startActivity(Intent(this, DynamicActivity::class.java))
    }

    fun useInList(view: View) {
        startActivity(Intent(this, ListActivity::class.java))
    }

    fun queue(view: View) {
        startActivity(Intent(this, UseQueueActivity::class.java))
    }

    fun vp(view: View) {
//        startActivity(Intent(this, AnimationFromFragmentActivity::class.java))
        startActivity(Intent(this, FragmentActivity::class.java))
    }

    fun gif(view: View) {
        startActivity(Intent(this, GifActivity::class.java))
    }
}