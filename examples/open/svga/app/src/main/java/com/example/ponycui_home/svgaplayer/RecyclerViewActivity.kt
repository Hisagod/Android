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

    private val fileData = mutableListOf<Any>()
    private val fileData1 = mutableListOf<Any>()
    private val fileData2 = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileData.add("file:///android_asset/x1.svga")
        fileData1.add("file:///android_asset/y1.svga")
        fileData2.add("file:///android_asset/z1.svga")

        fileData.add("file:///android_asset/x2.svga")
        fileData1.add("file:///android_asset/y2.svga")
        fileData2.add("file:///android_asset/z2.svga")

        fileData.add("file:///android_asset/x3.svga")
        fileData1.add("file:///android_asset/y3.svga")
        fileData2.add("file:///android_asset/z3.svga")

        fileData.add("file:///android_asset/x4.svga")
        fileData1.add("file:///android_asset/y4.svga")
        fileData2.add("file:///android_asset/z4.svga")

        fileData.add("file:///android_asset/x5.svga")
        fileData1.add("file:///android_asset/y5.svga")
        fileData2.add("file:///android_asset/z5.svga")

        fileData.add("file:///android_asset/x6.svga")
        fileData1.add("file:///android_asset/y6.svga")
        fileData2.add("file:///android_asset/z6.svga")

        fileData.add("file:///android_asset/x7.svga")
        fileData1.add("file:///android_asset/y7.svga")
        fileData2.add("file:///android_asset/z7.svga")

        fileData.add("file:///android_asset/x8.svga")
        fileData1.add("file:///android_asset/y8.svga")
        fileData2.add("file:///android_asset/z8.svga")

        fileData.add("file:///android_asset/x9.svga")
        fileData1.add("file:///android_asset/y9.svga")
        fileData2.add("file:///android_asset/z9.svga")

        fileData.add("file:///android_asset/x10.svga")
        fileData1.add("file:///android_asset/y10.svga")
        fileData2.add("file:///android_asset/z10.svga")

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

    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.my_item, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = 10

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val svg = holder.itemView.findViewById<AppCompatImageView>(R.id.svg)
//            svg.load(fileData[position])
            val svg1 = holder.itemView.findViewById<AppCompatImageView>(R.id.svg1)
//            svg1.load(fileData1[position])
            val svg2 = holder.itemView.findViewById<AppCompatImageView>(R.id.svg2)
            svg2.load(fileData2[position])
        }
    }
}