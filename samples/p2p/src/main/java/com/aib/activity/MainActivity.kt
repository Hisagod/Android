package com.aib.activity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aib.base.activity.BaseActivity
import com.aib.p2p.R
import com.aib.p2p.databinding.ActivityMainBinding
import com.aib.sdk.arouter.ArouterManager
import com.aib.sdk.arouter.ArouterPath
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Route(path = ArouterPath.PATH_MAIN)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var manager: ArouterManager

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {

        binding.vp.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 3
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int = 3

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> {
                            manager.openHomePage()
                        }
                        1 -> {
                            manager.openProductPage()
                        }
                        2 -> {
                            manager.openMinePage()
                        }
                        else -> manager.openHomePage()
                    }
                }
            }
        }

        binding.bnv.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_home -> {
                    binding.vp.setCurrentItem(0, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.btn_invest -> {
                    binding.vp.setCurrentItem(1, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.btn_assets -> {
                    binding.vp.setCurrentItem(2, false)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }
}