package com.hjc.wan.http.exception

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
        var error = "未知错误"
        if (e is UnknownHostException) {
            error = "网络不可用"
        } else if (e is SocketTimeoutException) {
            error = "请求网络超时"
        } else if (e is HttpException) {
            error = convertHttpCode(e)
        } else if (e is ParseException || e is JSONException) {
            error = "数据解析错误"
        } else if (e is ApiException) {
            error = convertServerCode(e)
        }
        return error
    }

    private fun convertHttpCode(httpException: HttpException): String {
        val msg: String
        if (httpException.code() in 500..599) {
            msg = "服务器处理请求出错"
        } else if (httpException.code() in 400..499) {
            msg = "服务器无法处理请求"
        } else if (httpException.code() in 300..399) {
            msg = "请求被重定向到其他页面"
        } else {
            msg = httpException.message()
        }
        return msg
    }

    private fun convertServerCode(apiException: ApiException): String {
        var msg = "未知请求错误"
        if (apiException.code == ServerCode.CODE_FAIL){
            msg = "请求失败"
        }else if(apiException.code == ServerCode.CODE_UN_LOGIN){
            msg = "请先登录"
        }
        return msg
    }
}
