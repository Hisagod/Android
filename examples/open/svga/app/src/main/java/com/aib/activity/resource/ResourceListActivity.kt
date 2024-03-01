package com.aib.activity.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ponycui_home.svgaplayer.R
import com.example.ponycui_home.svgaplayer.databinding.ActivityResourceListBinding
import com.opensource.svgaplayer.SVGAImageView

/**
 * 加载全部资源列表
 */
class ResourceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResourceListBinding
    private val data = mutableListOf<ResourceBean>()
    private val adapter by lazy { MyAdapter() }
    private val svga = "svga"
    private val png = "png"
    private val gif = "gif"
    private val webp = "webp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data.add(ResourceBean(svga, R.raw.castle))
        data.add(ResourceBean(svga, R.raw.food))
        data.add(ResourceBean(svga, R.raw.food_5_frame))
        data.add(ResourceBean(svga, R.raw.food_10_frame))
        data.add(ResourceBean(svga, R.raw.huolong))
        data.add(ResourceBean(svga, R.raw.ic_enter_room_loading))
        data.add(ResourceBean(svga, R.raw.matte_bitmap))
        data.add(ResourceBean(svga, R.raw.matte_rect))
        data.add(ResourceBean(svga, R.raw.rose))
        data.add(ResourceBean(svga, R.raw.rose_2_version))
        data.add(ResourceBean(svga, R.raw.test2_text_user))
        data.add(ResourceBean(png, R.raw.test3))
        data.add(ResourceBean(gif, R.raw.test4))
        data.add(ResourceBean(webp, R.raw.test5))
        data.add(ResourceBean(webp, R.raw.test6))
        data.add(ResourceBean(svga, R.raw.test7))
        data.add(ResourceBean(svga, R.raw.tomato))
        data.add(ResourceBean(svga, R.raw.x1))
        data.add(ResourceBean(svga, R.raw.x2))
        data.add(ResourceBean(svga, R.raw.x3))
        data.add(ResourceBean(svga, R.raw.x4))
        data.add(ResourceBean(svga, R.raw.x5))
        data.add(ResourceBean(svga, R.raw.x6))
        data.add(ResourceBean(svga, R.raw.x7))
        data.add(ResourceBean(svga, R.raw.x8))
        data.add(ResourceBean(svga, R.raw.x9))
        data.add(ResourceBean(svga, R.raw.x10))
        data.add(ResourceBean(svga, R.raw.y1))
        data.add(ResourceBean(svga, R.raw.y2))
        data.add(ResourceBean(svga, R.raw.y3))
        data.add(ResourceBean(svga, R.raw.y4))
        data.add(ResourceBean(svga, R.raw.y5))
        data.add(ResourceBean(svga, R.raw.y6))
        data.add(ResourceBean(svga, R.raw.y7))
        data.add(ResourceBean(svga, R.raw.y8))
        data.add(ResourceBean(svga, R.raw.y9))
        data.add(ResourceBean(svga, R.raw.y10))
        data.add(ResourceBean(svga, R.raw.z1))
        data.add(ResourceBean(svga, R.raw.z2))
        data.add(ResourceBean(svga, R.raw.z3))
        data.add(ResourceBean(svga, R.raw.z4))
        data.add(ResourceBean(svga, R.raw.z5))
        data.add(ResourceBean(svga, R.raw.z6))
        data.add(ResourceBean(svga, R.raw.z7))
        data.add(ResourceBean(svga, R.raw.z8))
        data.add(ResourceBean(svga, R.raw.z9))
        data.add(ResourceBean(svga, R.raw.z10))

        binding.rv.adapter = adapter
    }

    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tv: AppCompatTextView
            val svg: SVGAImageView

            init {
                tv = view.findViewById(R.id.desc)
                svg = view.findViewById(R.id.svg)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resource_list, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            data[position].apply {
                holder.tv.text = desc
                holder.svg.load(resource)
            }
        }
    }
}
