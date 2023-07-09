package com.aib.rep

import com.aib.bean.BaseBean
import com.aib.bean.UserBean
import com.aib.net.ApiService
import com.aib.sdk.sp.SpManager
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val api: ApiService,
        private val sp: SpManager
) {
    //用户登录
    suspend fun login(phone: String, pwd: String): BaseBean<UserBean> {
        return api.login(phone, pwd)
    }

    //用户注册
    suspend fun register(phone: String, pwd: String): BaseBean<Any> {
        return api.register(phone, pwd)
    }

    //保存用户信息
    fun saveUserInfo(userBean: UserBean) {
        with(sp) {
            putAvatar(userBean.avatar)
            putNickName(userBean.name)
            putUserId(userBean.id)
        }
    }
}