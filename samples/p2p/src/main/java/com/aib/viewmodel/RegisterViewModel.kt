package com.aib.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aib.net.Resource
import com.aib.net.convert
import com.aib.rep.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
        private val userRep: UserRepository
) : ViewModel() {
    fun userRegister(phone: String, pwd: String): MutableLiveData<Resource<Any>> {
        val data = MutableLiveData<Resource<Any>>()
        viewModelScope.launch {
            runCatching {
                val bean = userRep.register(phone, pwd).convert()
                data.value = Resource.success(bean)
            }.onFailure {
                data.value = Resource.error(it.message ?: "获取数据失败", null)
            }
        }
        return data
    }
}