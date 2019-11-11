package com.hjc.wan.http.observer

import com.hjc.wan.http.exception.ExceptionUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:52
 * @Description: 数据返回统一处理Observer基类
 */
abstract class BaseObserver<T> : Observer<T> {

    override fun onNext(response: T) {

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
