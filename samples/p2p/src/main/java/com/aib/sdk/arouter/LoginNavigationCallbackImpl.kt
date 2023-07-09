package com.aib.sdk.arouter

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter

class LoginNavigationCallbackImpl : NavigationCallback {
    override fun onFound(postcard: Postcard) {

    }

    override fun onLost(postcard: Postcard) {
    }

    override fun onArrival(postcard: Postcard) {
    }

    override fun onInterrupt(postcard: Postcard) {
        val path = postcard.path
        val extras = postcard.extras
        ARouter.getInstance().build(ArouterPath.PATH_LOGIN)
                .with(extras)
                .withString(ArouterKey.KEY_PATH, path)
                .navigation()
    }
}