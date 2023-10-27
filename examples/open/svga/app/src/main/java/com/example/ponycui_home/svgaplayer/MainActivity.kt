package com.example.ponycui_home.svgaplayer

import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ponycui_home.svgaplayer.add.AddJavaActivity
import com.example.ponycui_home.svgaplayer.bean.SampleItem
import com.example.ponycui_home.svgaplayer.databinding.ActivityAddJavaBinding
import com.example.ponycui_home.svgaplayer.databinding.ActivityMainBinding
import com.example.ponycui_home.svgaplayer.load.LoadFromAssetsActivity

class MainActivity : AppCompatActivity() {
    private val items: ArrayList<SampleItem> = ArrayList()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
        setupListView()
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

    private fun setupListView() {
        binding.listView.adapter = object : ListAdapter {
            override fun areAllItemsEnabled(): Boolean {
                return false
            }

            override fun isEnabled(i: Int): Boolean {
                return false
            }

            override fun registerDataSetObserver(dataSetObserver: DataSetObserver) {}
            override fun unregisterDataSetObserver(dataSetObserver: DataSetObserver) {}
            override fun getCount(): Int {
                return items.size
            }

            override fun getItem(i: Int): Any {
                return Unit
            }

            override fun getItemId(i: Int): Long {
                return i.toLong()
            }

            override fun hasStableIds(): Boolean {
                return false
            }

            override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
                val linearLayout = LinearLayout(this@MainActivity)
                val textView = TextView(this@MainActivity)
                textView.setOnClickListener {
                    this@MainActivity.startActivity(
                        items[i]!!.intent
                    )
                }
                textView.text = items[i]!!.title
                textView.textSize = 24f
                textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                linearLayout.addView(
                    textView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (55 * resources.displayMetrics.density).toInt()
                    )
                )
                return linearLayout
            }

            override fun getItemViewType(i: Int): Int {
                return 1
            }

            override fun getViewTypeCount(): Int {
                return 1
            }

            override fun isEmpty(): Boolean {
                return false
            }
        }
        binding.listView.setBackgroundColor(Color.WHITE)
    }
}