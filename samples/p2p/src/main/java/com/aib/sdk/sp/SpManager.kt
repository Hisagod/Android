package com.aib.sdk.sp

import com.blankj.utilcode.util.AppUtils
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class SpManager @Inject constructor() {

    private fun getMMKV() = MMKV.mmkvWithID(AppUtils.getAppPackageName(), MMKV.MULTI_PROCESS_MODE)

    fun putUserId(uid: String) {
        getMMKV()?.encode(SpKeyConstant.KEY_STRING_USER_ID, uid)
    }

    fun getUserId(): String? {
        return getMMKV()?.decodeString(SpKeyConstant.KEY_STRING_USER_ID)
    }

    fun putNickName(name: String) {
        getMMKV()?.encode(SpKeyConstant.KEY_STRING_USER_NAME, name)
    }

    fun getNickName(): String? {
        return getMMKV()?.decodeString(SpKeyConstant.KEY_STRING_USER_NAME)
    }

    fun putAvatar(url: String) {
        getMMKV()?.encode(SpKeyConstant.KEY_STRING_USER_AVATAR, url)
    }

    fun getAvatar(): String? {
        return getMMKV()?.decodeString(SpKeyConstant.KEY_STRING_USER_AVATAR)
    }

    fun clearAllData() {
        getMMKV()?.clearAll()
    }
}