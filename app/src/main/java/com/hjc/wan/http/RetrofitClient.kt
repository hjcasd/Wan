package com.hjc.wan.http

import com.hjc.wan.http.config.HttpConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:55
 * @Description: Retrofit封装
 */
object RetrofitClient {
    private val mApi: Api

    init {
        val builder = HttpClient.getBuilder()

        val retrofit = Retrofit.Builder()
            .baseUrl(HttpConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(builder.build())
            .build()

        mApi = retrofit.create(Api::class.java)
    }

    fun getApi(): Api {
        return mApi
    }

}
