package com.example.kotlin

import kotlinx.coroutines.delay
import javax.inject.Inject

class MainRepository @Inject constructor() {
    suspend fun login(phone: String, pwd: String): UserBean {
        delay(3000)
        return UserBean(phone, pwd)
    }
}