package com.example.ponycui_home.svgaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.Disposable
import com.example.ponycui_home.svgaplayer.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerviewBinding
    private val myAdapter by lazy { MyAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.adapter = myAdapter

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    application.getCustomInterceptor()?.loaderFlow?.value = true
                } else {
                    application.getCustomInterceptor()?.loaderFlow?.value = false
                }
            }
        })
    }

    class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.my_item, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = 1000

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val svg = holder.itemView.findViewById<AppCompatImageView>(R.id.svg)
            val job = svg.load("file:///android_asset/test.svga")
        }
    }
}