package com.hjc.wan.http.helper


import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.ObservableTransformer

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:48
 * @Description: 线程切换及绑定生命周期
 */
object RxHelper {
    fun <T> bind(provider: LifecycleProvider<*>): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                .compose(RxSchedulers.ioToMain())
                .compose(provider.bindToLifecycle())
        }
    }
}
