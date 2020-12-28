package com.hjc.wan.http.exception

import com.blankj.utilcode.util.LogUtils
import org.json.JSONException
import retrofit2.HttpException

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:48
 * @Description: 对返回的错误进行处理
 */
object ExceptionUtils {
    fun handleException(e: Throwable): String {
        LogUtils.e("error: $e")
        return when (e) {
            is UnknownHostException -> "网络不可用"

            is SocketTimeoutException -> "请求网络超时"

            is HttpException -> convertHttpCode(e)

            is JSONException, is ParseException -> "数据解析错误"

            is ApiException -> convertServerCode(e)

            else -> "未知错误"
        }
    }

    private fun convertHttpCode(e: HttpException): String {
        return when (e.code()) {
            in 500..599 -> "服务器处理请求出错"

            in 400..499 -> "服务器无法处理请求"

            in 300..399 -> "请求被重定向到其他页面"

            else -> e.message()
        }
    }

    private fun convertServerCode(e: ApiException): String {
        return when (e.code) {
            ServerCode.CODE_FAIL -> e.message.toString()

            ServerCode.CODE_UN_LOGIN -> "登录失效,请重新登录"

            else -> "未知请求"
        }
    }
}
