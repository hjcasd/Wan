package com.hjc.wan.http.exception

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:47
 * @Description: 自定义异常类(处理服务器code码异常)
 */
class ApiException(msg: String?, val code: String?) : Exception(msg)