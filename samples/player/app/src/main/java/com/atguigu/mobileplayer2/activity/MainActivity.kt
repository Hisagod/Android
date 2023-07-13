package com.atguigu.mobileplayer2.activity

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.atguigu.mobileplayer2.R
import com.atguigu.mobileplayer2.databinding.ActivityMainBinding
import com.atguigu.mobileplayer2.pager.AudioPager
import com.atguigu.mobileplayer2.pager.VideoPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    /**
     * 页面的集合
     */
    private var basePagers: ArrayList<Fragment>? = null

    /**
     * 选中的位置
     */
    private var position = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        basePagers = ArrayList()
        basePagers!!.add(VideoPager()) //添加本地视频页面-0
        basePagers!!.add(AudioPager()) //添加本地音乐页面-1
        binding.vp.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return basePagers!![position]
            }

            override fun getCount(): Int {
                return basePagers!!.size
            }
        }
        binding.vp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bnvMain.selectedItemId = R.id.btn_local_video
                    1 -> binding.bnvMain.selectedItemId = R.id.btn_local_audio
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.bnvMain.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.btn_local_video) {
                binding.vp.setCurrentItem(0, false)
                position = 0
                return@OnNavigationItemSelectedListener true
            } else if (itemId == R.id.btn_local_audio) {
                binding.vp.setCurrentItem(1, false)
                position = 1
                return@OnNavigationItemSelectedListener true
            }
            false
        })
    }

    /**
     * 是否已经退出
     */
    private var isExit = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (position != 0) { //不是第一页面
                position = 0
                //选中第一页
                binding!!.vp.setCurrentItem(0, false)
                binding!!.bnvMain.selectedItemId = R.id.btn_local_video
                return true
            } else if (!isExit) {
                isExit = true
                Toast.makeText(this@MainActivity, "再按一次推出", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ isExit = false }, 2000)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}