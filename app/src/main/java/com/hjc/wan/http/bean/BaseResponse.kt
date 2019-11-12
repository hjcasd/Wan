package com.hjc.wan.http.bean

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:46
 * @Description: 通用返回Json封装
 */
class BaseResponse<T> {
    var errorCode: Int? = null
    var errorMsg: String? = null
    var data: T? = null

    override fun toString(): String {
        return "BaseResponse{errorCode=$errorCode, errorMsg='$errorMsg', data=$data}"
    }
}
