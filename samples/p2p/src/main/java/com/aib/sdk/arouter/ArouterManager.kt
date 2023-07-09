package com.aib.sdk.arouter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ArouterManager @Inject constructor(
    private val ctx: Context
) {

    fun openHomePage(): Fragment {
        return ARouter.getInstance().build(ArouterPath.PATH_HOME_PAGE).navigation() as Fragment
    }

    fun openProductPage(): Fragment {
        return ARouter.getInstance().build(ArouterPath.PATH_PRODUCT_PAGE).navigation() as Fragment
    }

    fun openMinePage(): Fragment {
        return ARouter.getInstance().build(ArouterPath.PATH_MINE_PAGE).navigation() as Fragment
    }

    fun openMainPage() {
        ARouter.getInstance().build(ArouterPath.PATH_MAIN).navigation()
    }

    fun openRegister() {
        ARouter.getInstance().build(ArouterPath.PATH_REGISTER).navigation()
    }

    fun openLogin() {
        ARouter.getInstance().build(ArouterPath.PATH_LOGIN).navigation()
    }

    fun openUserInfo() {
        ARouter.getInstance().build(ArouterPath.PATH_USER_INFO).navigation()
    }

    fun openNext(path: String, bundle: Bundle) {
        ARouter.getInstance().build(path).with(bundle)
            .navigation(ctx, LoginNavigationCallbackImpl())
    }

    fun openNext(path: String) {
        ARouter.getInstance().build(path).navigation(ctx, LoginNavigationCallbackImpl())
    }
}