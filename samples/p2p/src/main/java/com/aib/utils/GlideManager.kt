package com.aib.utils

import android.widget.ImageView
import com.aib.net.NetConstants
import com.aib.p2p.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GlideManager {
    companion object {
        val getInstance: GlideManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideManager()
        }
    }

    /**
     * 从网络加载图片，并用于普通显示
     */
    fun loadCommonNetPath(view: ImageView, url: String) {
        Glide.with(view.context)
                .load(NetConstants.BASE_URL + url).into(view)
    }

    fun loadAvatarNetPath(view: ImageView, url: String) {
        Glide.with(view.context)
                .setDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_avatar))
                .load(NetConstants.BASE_URL + url)
                .into(view)
    }

    fun loadAvatarLocalPath(view: ImageView, url: String) {
        Glide.with(view.context)
                .setDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_avatar))
                .load(url)
                .into(view)
    }
}