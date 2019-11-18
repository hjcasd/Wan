package com.hjc.wan.http.interceptor

import android.text.TextUtils
import com.hjc.wan.utils.helper.AccountManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*


/**
 * @Author: HJC
 * @Date: 2019/3/11 17:39
 * @Description: 登录后保存cookie到SharedPreferences中
 */
class ReceivedCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        //获取请求返回的cookie
        if (!originalResponse.headers(COOKIE_HEADER).isEmpty()) {
            val cookieList = originalResponse.headers(COOKIE_HEADER)

            // 返回cookie
            if (!TextUtils.isEmpty(cookieList.toString())) {
                val oldCookie = AccountManager.getCookie()
                val cookieHashMap = HashMap<String, String>()

                // 之前存过cookie
                if (!TextUtils.isEmpty(oldCookie)) {
                    val cookies = oldCookie.split(";".toRegex()).dropLastWhile({ it.isEmpty() })
                        .toTypedArray()
                    for (cookie in cookies) {
                        if (cookie.contains("=")) {
                            val split = cookie.split("=".toRegex()).dropLastWhile({ it.isEmpty() })
                                .toTypedArray()
                            cookieHashMap[split[0]] = split[1]
                        } else {
                            cookieHashMap[cookie] = ""
                        }
                    }
                }

                //将cookie存到map中
                for (cookie in cookieList) {
                    val splits =
                        cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    // 存到Map里
                    for (str in splits) {
                        val split =
                            str.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (split.size == 2) {
                            cookieHashMap[split[0]] = split[1]
                        } else {
                            cookieHashMap[split[0]] = ""
                        }
                    }
                }

                // 取出来
                val sb = StringBuilder()
                if (cookieHashMap.size > 0) {
                    for (key in cookieHashMap.keys) {
                        sb.append(key)
                        val value = cookieHashMap[key]
                        if (!TextUtils.isEmpty(value)) {
                            sb.append("=")
                            sb.append(value)
                        }
                        sb.append(";")
                    }
                }
                AccountManager.setCookie(sb.toString())
            }
        }
        return originalResponse
    }

    companion object {
        private val COOKIE_HEADER = "Set-Cookie"
    }
}
