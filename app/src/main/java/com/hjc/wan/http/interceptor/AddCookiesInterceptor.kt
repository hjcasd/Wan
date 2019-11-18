package com.hjc.wan.http.interceptor


import com.hjc.wan.utils.helper.AccountManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @Author: HJC
 * @Date: 2019/3/11 17:40
 * @Description: 将登录后的cookie添加到请求中
 */
class AddCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val cookie = AccountManager.getCookie()
        builder.addHeader("Cookie", cookie)
        return chain.proceed(builder.build())
    }
}
