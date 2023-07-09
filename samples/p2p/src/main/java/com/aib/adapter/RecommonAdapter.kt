package com.aib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aib.bean.HomeBean
import com.aib.p2p.databinding.ItemHomeProductBinding
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder

class RecommonAdapter : QuickDataBindingItemBinder<HomeBean.ProductBean, ItemHomeProductBinding>() {
    override fun convert(holder: BinderDataBindingHolder<ItemHomeProductBinding>, data: HomeBean.ProductBean) {
        holder.dataBinding.product = data
    }

    override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemHomeProductBinding {
        return ItemHomeProductBinding.inflate(layoutInflater, parent, false)
    }
}