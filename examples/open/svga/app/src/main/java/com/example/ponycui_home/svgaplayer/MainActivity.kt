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
import com.example.ponycui_home.svgaplayer.add.AddJavaActivity
import com.example.ponycui_home.svgaplayer.bean.SampleItem
import com.example.ponycui_home.svgaplayer.databinding.ActivityAddJavaBinding
import com.example.ponycui_home.svgaplayer.databinding.ActivityMainBinding
import com.example.ponycui_home.svgaplayer.load.LoadFromAssetsActivity

class MainActivity : AppCompatActivity() {
    private val items: ArrayList<SampleItem> = ArrayList()
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { MyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
        initRv()
    }

    private fun initRv() {
        binding.rv.adapter = adapter
    }

    private fun setupData() {
        items.add(
            SampleItem(
                "Animation From Java Code",
                Intent(this, AnimationFromJavaCodeActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation From Assets",
                Intent(this, LoadFromAssetsActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation From Network",
                Intent(this, AnimationFromNetworkActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation With Dynamic Image",
                Intent(this, AnimationWithDynamicImageActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation With Dynamic Click",
                Intent(this, AnimationFromClickActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation From RecyclerView",
                Intent(this, AnimationFromRecyclerViewActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation From Fragment",
                Intent(this, AnimationFromFragmentActivity::class.java)
            )
        )
        items.add(
            SampleItem(
                "Animation From Lazy Fragment",
                Intent(this, AnimationFromLazyFragmentActivity::class.java)
            )
        )
        items.add(SampleItem("Animation Gif", Intent(this, AnimationGifActivity::class.java)))
        items.add(SampleItem("Png", Intent(this, PngActivity::class.java)))

        //添加方式
        items.add(
            SampleItem(
                "Add Java Activity",
                Intent(this, AddJavaActivity::class.java)
            )
        )
    }

    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.itemView.findViewById<AppCompatTextView>(R.id.tv).apply {
                text = items[position].title
            }.setOnClickListener {
                startActivity(items[position].intent)
            }
        }


        inner class MyViewHolder(view: View) : ViewHolder(view)
    }
}