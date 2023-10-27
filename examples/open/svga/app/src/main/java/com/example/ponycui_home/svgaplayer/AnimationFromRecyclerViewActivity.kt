package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ponycui_home.svgaplayer.databinding.ActivityFromRecyclerviewBinding
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

        inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.my_item, parent, false)
            return MyViewHolder(view)
        }

        override fun onViewAttachedToWindow(holder: MyViewHolder) {
            super.onViewAttachedToWindow(holder)

            val svg = holder.itemView.findViewById<SVGAImageView>(R.id.svg)
            svg.load("file:///android_asset/test7.svga") {
                target(SVGATarget(svg) {
                    it.startAnimation()
                })
            }
        }

        override fun onViewDetachedFromWindow(holder: MyViewHolder) {
            super.onViewDetachedFromWindow(holder)
            val svg = holder.itemView.findViewById<SVGAImageView>(R.id.svg)
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)

        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
        }

        override fun getItemCount(): Int = 1000

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        }
    }
}