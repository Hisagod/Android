package com.aib.sdk.arouter

object ArouterPath {
    private const val PATH_APP = "/app/"
    const val PATH_SPLASH = PATH_APP + "SplashActivity"
    const val PATH_REGISTER = PATH_APP + "RegistActivity"
    const val PATH_LOGIN = PATH_APP + "LoginActivity"
    const val PATH_MAIN = PATH_APP + "MainActivity"

    //设置中心
    const val PATH_SETTINGS = PATH_APP + "SettingsActivity"

    //用户信息
    const val PATH_USER_INFO = PATH_APP + "UserInfoActivity"

    //首页
    const val PATH_HOME_PAGE = PATH_APP + "HomeFragment"

    //商品列表
    const val PATH_PRODUCT_PAGE = PATH_APP + "ProductFragment"

    //我的
    const val PATH_MINE_PAGE = PATH_APP + "CenterFragment"
}

object ArouterKey {
    const val KEY_PATH = "path"
}