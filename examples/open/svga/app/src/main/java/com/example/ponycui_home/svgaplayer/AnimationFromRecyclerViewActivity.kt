package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromRecyclerviewBinding
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGATarget

class AnimationFromRecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromRecyclerviewBinding
    private val myAdapter by lazy { MyAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.adapter = myAdapter
    }

    class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.my_item, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = 1

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val svg = holder.itemView.findViewById<AppCompatImageView>(R.id.svg)
            svg.load("file:///android_asset/test7.svga") {
                target(ImageTarget(svg))
            }
        }
    }
}