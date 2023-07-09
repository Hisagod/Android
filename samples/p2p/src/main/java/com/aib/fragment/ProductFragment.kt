package com.aib.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.aib.base.fragment.BaseLazyFragment
import com.aib.bean.ProductBean
import com.aib.net.Status
import com.aib.p2p.R
import com.aib.p2p.databinding.FragmentProductlistBinding
import com.aib.sdk.arouter.ArouterPath
import com.aib.viewmodel.MainViewModel
import com.aib.widget.RoundProgress
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_PRODUCT_PAGE)
class ProductFragment : BaseLazyFragment<FragmentProductlistBinding>() {
    private val vm by activityViewModels<MainViewModel>()
    private lateinit var adapter: BaseQuickAdapter<ProductBean, BaseViewHolder>
    override fun getLayoutId(): Int {
        return R.layout.fragment_productlist
    }

    override fun initData() {
        adapter = object : BaseQuickAdapter<ProductBean, BaseViewHolder>(R.layout.item_product) {
            override fun convert(holder: BaseViewHolder, item: ProductBean) {
                holder.setText(R.id.p_name, item.name)
                holder.setText(R.id.p_money, item.money.toString())
                holder.setText(R.id.p_yearlv, item.yearRate)
                holder.setText(R.id.p_suodingdays, item.suodingDays.toString())
                holder.setText(R.id.p_minzouzi, item.minTouMoney.toString())
                holder.setText(R.id.p_minnum, item.menberNum.toString())

                holder.getView<RoundProgress>(R.id.p_progresss).progress = item.progress
            }
        }
        binding.rv.adapter = adapter

        loadData()
    }

    private fun loadData() {
        vm.getProductList().observe(this, Observer {
            when (it.status) {
                Status.LOAD -> {
                }
                Status.SUCCESS -> {
                    adapter.setNewInstance(it.data as MutableList<ProductBean>?)
                }
                Status.ERROR -> {

                }
                Status.EMPTY -> {
                }
            }
        })
    }
}