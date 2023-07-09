package com.aib.fragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.aib.adapter.RecommonAdapter
import com.aib.base.fragment.BaseLazyFragment
import com.aib.bean.HomeBean
import com.aib.net.Status
import com.aib.p2p.R
import com.aib.p2p.databinding.FragmentHomeBinding
import com.aib.sdk.arouter.ArouterPath
import com.aib.viewmodel.MainViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseBinderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = ArouterPath.PATH_HOME_PAGE)
class HomeFragment : BaseLazyFragment<FragmentHomeBinding>() {
    //使用Activity的VM
    private val vm by activityViewModels<MainViewModel>()

    private val homeAdapter = BaseBinderAdapter()

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initData() {
        binding.rv.adapter = homeAdapter
        homeAdapter.addItemBinder(HomeBean::class.java, com.aib.adapter.BannerAdapter())
        homeAdapter.addItemBinder(HomeBean.ProductBean::class.java, RecommonAdapter())
        loadData()
        refresh()
    }

    private fun refresh() {
        binding.srl.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        vm.mainData.observe(this) {
            binding.resource = it
            if (it.status == Status.SUCCESS) {
                val dataItem = mutableListOf<Any>()
                it.data?.let { bean ->
                    dataItem.add(bean)
                    dataItem.add(bean.product)
                }
                homeAdapter.setList(dataItem)
            }
            binding.srl.finishRefresh()
        }
        vm.getHome()
    }
}