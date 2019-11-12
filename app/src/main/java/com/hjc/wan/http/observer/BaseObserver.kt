package com.hjc.wan.http.observer

import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.http.exception.ApiException
import com.hjc.wan.http.exception.ExceptionUtils
import com.hjc.wan.http.exception.ServerCode
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:52
 * @Description: 数据返回统一处理Observer基类
 */
abstract class BaseObserver<T> : Observer<BaseResponse<T>> {

    override fun onNext(response: BaseResponse<T>) {
        if (ServerCode.CODE_SUCCESS == response.errorCode) {
            // 请求成功
            onSuccess(response.data)
        } else {
            // 请求成功,Code错误,抛出ApiException
            onError(ApiException(response.errorMsg, response.errorCode))
        }
    }

    override fun onError(e: Throwable) {
        onFailure(ExceptionUtils.handleException(e))
    }

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    abstract fun onSuccess(result: T?)

    abstract fun onFailure(errorMsg: String)

}
