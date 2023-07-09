package com.aib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aib.bean.HomeBean
import com.aib.bean.ProductBean
import com.aib.net.Resource
import com.aib.net.convert
import com.aib.rep.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRep: MainRepository
) : ViewModel() {

    val mainData = MutableLiveData<Resource<HomeBean>>()

    /**
     * 获取首页数据
     */
    fun getHome() {
        mainData.value = Resource.loading(mainData.value?.data)
        viewModelScope.launch {
            try {
                val bean = mainRep.loadHomeDataFromNet().convert()
                mainData.value = Resource.success(bean)
            } catch (e: Exception) {
                mainData.value = Resource.error(e.message ?: "加载失败", null)
            }
        }
    }

    /**
     * 获取产品列表
     */
    fun getProductList(): LiveData<Resource<List<ProductBean>>> {
        val data = MutableLiveData<Resource<List<ProductBean>>>()
        data.value = Resource.loading(null)//加载中
        viewModelScope.launch {
            runCatching {
                val bean = mainRep.loadProduct().convert()
                if (bean.isNullOrEmpty()) {
                    data.value = Resource.empty()//加载数据为空
                } else {
                    data.value = Resource.success(bean)//加载数据成功
                }
            }.onFailure {
                data.value = Resource.error(it.message ?: "加载失败", null)//加载数据失败
            }
        }
        return data
    }
}