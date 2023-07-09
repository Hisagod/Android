package com.aib.utils

import com.aib.sdk.sp.SpKeyConstant
import com.blankj.utilcode.util.SPStaticUtils

class UserUtils {
    companion object {
        /**
         * 用户是否登录
         */
        fun isLogin(): Boolean {
            val uid = SPStaticUtils.getInt(SpKeyConstant.KEY_STRING_USER_ID, 0)
            return uid != 0
        }
    }
}