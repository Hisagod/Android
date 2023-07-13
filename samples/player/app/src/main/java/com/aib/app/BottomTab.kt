package com.aib.app

import androidx.annotation.DrawableRes
import com.atguigu.mobileplayer2.R


sealed class BottomTab(val route: String, val text: String, @DrawableRes val icon: Int) {
    object VideoTab : BottomTab(RouterConstant.TAB_VIDEO, "视频", R.drawable.ic_tab_video)
    object AudioTab : BottomTab(RouterConstant.TAB_AUDIO, "音频", R.drawable.ic_tab_audio)
}
