package com.aib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aib.bean.HomeBean
import com.aib.p2p.databinding.ItemBannerBinding
import com.aib.utils.GlideManager
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class BannerAdapter : QuickDataBindingItemBinder<HomeBean, ItemBannerBinding>() {
    override fun convert(holder: BinderDataBindingHolder<ItemBannerBinding>, data: HomeBean) {
        val urls = mutableListOf<String>()
        data.banner.forEach {
            urls.add(it.img)
        }
        holder.dataBinding.banner.apply {
            adapter = object : BannerImageAdapter<String>(urls) {
                override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                    holder?.imageView?.let { imageview ->
                        GlideManager.getInstance.loadCommonNetPath(imageview, data ?: "")
                    }
                }
            }
        }
    }

    override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemBannerBinding {
        return ItemBannerBinding.inflate(layoutInflater, parent, false)
    }
}