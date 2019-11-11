package com.hjc.wan.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:52
 * @Description: Token拦截器
 */
class TokenInterceptor(private val token: String?) : Interceptor {
    companion object {
        private const val USER_TOKEN = "Authorization"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (token == null || originalRequest.header(USER_TOKEN) != null) {
            return chain.proceed(originalRequest)
        }
        val request = originalRequest.newBuilder()
            .header(USER_TOKEN, token)
            .build()
        return chain.proceed(request)
    }
}
