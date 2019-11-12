package com.hjc.wan.http.interceptor

import com.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:51
 * @Description: 日志拦截器
 */
class LogInterceptor : Interceptor {
    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val source = response.body()!!.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()


        val requestJson = "request code === ${response.code()}" +
                "\nrequest url === ${request.url()}" +
                "\nrequest duration === ${response.receivedResponseAtMillis() - response.sentRequestAtMillis()}ms" +
                "\nrequest header === ${request.headers()}" +
                "\nrequest body === ${bodyToString(request.body())}"
        LogUtils.e(requestJson)

        val responseJson = buffer.clone().readString(UTF8)
        FormatUtils.formatJsonAndLog(responseJson)

        return response
    }

    /**
     * 请求体转String
     *
     * @param request 请求体
     * @return String 类型的请求体
     */
    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            request!!.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: Exception) {
            return "no request body"
        }

    }
}
