package com.hjc.wan.http

import com.hjc.wan.BuildConfig
import com.hjc.wan.http.config.HttpConfig
import com.hjc.wan.http.interceptor.AddCookiesInterceptor
import com.hjc.wan.http.interceptor.LogInterceptor
import com.hjc.wan.http.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:55
 * @Description: Retrofit封装
 */
object RetrofitClient {
    private var mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(HttpConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getBuilder().build())
            .build()
    }

    fun getApi(): Api {
        return mRetrofit.create(Api::class.java)
    }

    private fun getBuilder(): OkHttpClient.Builder {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.connectTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(ReceivedCookiesInterceptor())
            .addInterceptor(AddCookiesInterceptor())

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LogInterceptor())
        }
        return builder
    }

}
