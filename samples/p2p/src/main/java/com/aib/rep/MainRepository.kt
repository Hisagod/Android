package com.aib.rep

import com.aib.bean.BaseBean
import com.aib.bean.HomeBean
import com.aib.bean.ProductBean
import com.aib.net.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ApiService) {

    suspend fun loadHomeDataFromNet(): BaseBean<HomeBean> {
        return api.getHome()
    }

    suspend fun loadProduct(): BaseBean<List<ProductBean>> {
        return api.getProductList()
    }
}