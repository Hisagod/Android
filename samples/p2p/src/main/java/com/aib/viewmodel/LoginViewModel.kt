package com.aib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aib.bean.UserBean
import com.aib.net.Resource
import com.aib.net.convert
import com.aib.rep.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val userRep: UserRepository
) : ViewModel() {
    fun login(phone: String, pwd: String): LiveData<Resource<UserBean>> {
        val data = MutableLiveData<Resource<UserBean>>()
        viewModelScope.launch {
            runCatching {
                val bean = userRep.login(phone, pwd).convert()
                //保存用户信息
                userRep.saveUserInfo(bean)
                data.value = Resource.success(bean)
            }.onFailure {
                data.value = Resource.error(it.message ?: "加载失败", null)
            }
        }
        return data
    }
}