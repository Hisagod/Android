package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
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

    fun loadSourceFrom(view: View) {
        startActivity(Intent(this, LoadSourceFromActivity::class.java))
    }

    fun svgaClick(view: View) {
        startActivity(Intent(this, ClickActivity::class.java))
    }

    fun useInList(view: View) {
        startActivity(Intent(this, RecyclerViewActivity::class.java))
    }

    fun queue(view: View) {
        startActivity(Intent(this, UseQueueActivity::class.java))
    }

    fun vp(view: View) {
//        startActivity(Intent(this, AnimationFromFragmentActivity::class.java))
        startActivity(Intent(this, AnimationFromLazyFragmentActivity::class.java))
    }

    fun gif(view: View) {
        startActivity(Intent(this, GifActivity::class.java))
    }
}