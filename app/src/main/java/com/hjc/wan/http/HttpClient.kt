package com.hjc.wan.http

import com.hjc.wan.BuildConfig
import com.hjc.wan.http.config.HttpConfig
import com.hjc.wan.http.interceptor.AddCookiesInterceptor
import com.hjc.wan.http.interceptor.LogInterceptor
import com.hjc.wan.http.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:55
 * @Description: OkHttp封装
 */
object HttpClient {

    private val mBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    init {
        mBuilder.connectTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.HTTP_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(ReceivedCookiesInterceptor())
            .addInterceptor(AddCookiesInterceptor())

        if (BuildConfig.IS_DEBUG) {
            mBuilder.addInterceptor(LogInterceptor())
        }
    }

    fun getBuilder(): OkHttpClient.Builder {
        return mBuilder
    }
}
