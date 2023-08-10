package com.aib.app.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RequestPermission(success: @Composable () -> Unit, fail: () -> Unit) {
    var data by remember {
        mutableStateOf(false)
    }

    PermissionUtils.permission(PermissionConstants.STORAGE)
        .callback { isAllGranted, granted, deniedForever, denied ->
            data = isAllGranted
        }.request()

    if (data) {
        LogUtils.e("获取")
    } else {
        LogUtils.e("没获取")
    }
}