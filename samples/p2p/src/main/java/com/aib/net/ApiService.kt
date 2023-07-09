package com.aib.net

import com.aib.bean.BaseBean
import com.aib.bean.HomeBean
import com.aib.bean.ProductBean
import com.aib.bean.UserBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    /**
     * 用户注册
     */
    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(@Field("phone") phone: String, @Field("pwd") pwd: String): BaseBean<Any>

    //获取Banner数据
    @GET("banner/getHome")
    suspend fun getHome(): BaseBean<HomeBean>

    //用户登录
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("phone") phone: String, @Field("pwd") pwd: String): BaseBean<UserBean>

    @GET("product/getProduct")
    suspend fun getProductList(): BaseBean<List<ProductBean>>
}