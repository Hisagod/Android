package com.aib.rep

import com.aib.net.ApiService
import com.blankj.utilcode.util.LogUtils
import javax.inject.Inject

class SplashRepository @Inject constructor(private val api: ApiService) {
    fun loadDataFromNet() {
        LogUtils.e(api.toString())
    }
}