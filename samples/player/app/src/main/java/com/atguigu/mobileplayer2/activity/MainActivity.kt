package com.atguigu.mobileplayer2.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.atguigu.mobileplayer2.R;
import com.atguigu.mobileplayer2.databinding.ActivityMainBinding;
import com.atguigu.mobileplayer2.pager.AudioPager;
import com.atguigu.mobileplayer2.pager.VideoPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2016/7/16 10:26
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页面
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 页面的集合
     */
    private ArrayList<Fragment> basePagers;

    /**
     * 选中的位置
     */
    private int position;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager());//添加本地视频页面-0
        basePagers.add(new AudioPager());//添加本地音乐页面-1

        binding.vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return basePagers.get(position);
            }

            @Override
            public int getCount() {
                return basePagers.size();
            }
        });

        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bnvMain.setSelectedItemId(R.id.btn_local_video);
                        break;
                    case 1:
                        binding.bnvMain.setSelectedItemId(R.id.btn_local_audio);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.bnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.btn_local_video) {
                    binding.vp.setCurrentItem(0, false);
                    position = 0;
                    return true;
                } else if (itemId == R.id.btn_local_audio) {
                    binding.vp.setCurrentItem(1, false);
                    position = 1;
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 是否已经退出
     */
    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (position != 0) {//不是第一页面
                position = 0;
                //选中第一页
                binding.vp.setCurrentItem(0, false);
                binding.bnvMain.setSelectedItemId(R.id.btn_local_video);
                return true;
            } else if (!isExit) {
                isExit = true;
                Toast.makeText(MainActivity.this, "再按一次推出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
