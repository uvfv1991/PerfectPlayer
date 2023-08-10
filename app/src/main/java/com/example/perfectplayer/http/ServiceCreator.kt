package com.example.perfectplayer.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient = OkHttpClient
        .Builder()
        .addInterceptor(httpLoggingInterceptor) // 添加拦截器

    private val builder = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    /*回的值是对象本身
    apply一般用于一个对象实例初始化的时候，需要对对象中的属性进行赋值。或者动态inflate出一个XML的View的时候需要给View绑定数据也会用到，这种情景非常常见。
    特别是在我们开发中会有一些数据model向View model转化实例化的过程中需要用到。*/
    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
