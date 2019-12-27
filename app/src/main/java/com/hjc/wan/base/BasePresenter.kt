package com.hjc.wan.base

import java.lang.ref.WeakReference

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:03
 * @Description: Presenter层基类
 */
abstract class BasePresenter<V : IBaseView> {

    /**
     * 持有UI接口的弱引用
     */
    private lateinit var mViewRef: WeakReference<V>

    fun attachView(view: V) {
        mViewRef = WeakReference(view)
    }

    fun getView(): V? {
        return mViewRef.get()
    }

    /**
     * 解绑
     * 在onDestroy方法中调用，防止内存泄漏
     */
    fun detachView() {
        mViewRef.clear()
    }

}
