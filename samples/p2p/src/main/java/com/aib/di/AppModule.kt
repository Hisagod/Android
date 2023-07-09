package com.aib.di

import android.content.Context
import com.aib.net.ApiService
import com.aib.net.NetConstants
import com.aib.sdk.arouter.ArouterManager
import com.aib.sdk.sp.SpManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Api {
    private val client = {
        OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))

                .build()
    }

    @Singleton
    @Provides
    fun getApiService(): ApiService {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConstants.BASE_URL)
                .client(client())
                .build()
                .create(ApiService::class.java)
    }

    //Arouter管理器
    @Singleton
    @Provides
    fun getArouterManager(@ApplicationContext ctx: Context): ArouterManager = ArouterManager(ctx)

    //K-V模式SP对象
    @Singleton
    @Provides
    fun getSpManager(): SpManager = SpManager()
}