package com.aib.net

import com.aib.bean.BaseBean
import com.blankj.utilcode.util.ActivityUtils

/**
 * 扩展BaseBean函数，进行数据转换
 */
fun <D> BaseBean<D>.convert(): D {
    when (code) {
        -1 -> {
            //服务器出错
            throw Exception("服务器异常")
        }
        0 -> return data
        1 -> {
            //去注册
            finishActivity()
//            ARouter.getInstance().build(com.aib.sdk.arouter.ArouterPath.PATH_REGISTER).navigation()
            throw Exception(msg)
        }
        2 -> {
            //已注册，去登录
            finishActivity()
//            ARouter.getInstance().build(com.aib.sdk.arouter.ArouterPath.PATH_LOGIN).navigation()
            throw Exception(msg)
        }
        3 -> {
            //密码错误
            throw Exception(msg)
        }
        else -> throw Exception("其它问题")
    }
}

private fun finishActivity() {
    val topActivity = ActivityUtils.getTopActivity()
//    val navigation = ARouter.getInstance().build(com.aib.sdk.arouter.ArouterPath.PATH_MAIN).navigation()

//    if (topActivity != navigation) {
//        topActivity.finish()
//    }
}