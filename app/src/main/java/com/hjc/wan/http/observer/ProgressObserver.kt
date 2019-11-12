package com.hjc.wan.http.observer

import android.support.v4.app.FragmentManager
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.http.exception.ApiException
import com.hjc.wan.http.exception.ExceptionUtils
import com.hjc.wan.http.exception.ServerCode
import com.hjc.wan.widget.dialog.LoadingDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:52
 * @Description: 带进度的Observer
 */
abstract class ProgressObserver<T> : Observer<BaseResponse<T>> {
    private var mLoadingDialog: LoadingDialog
    private var mFragmentManager: FragmentManager
    private var isShowLoading = true

    constructor(fragmentManager: FragmentManager) {
        this.mFragmentManager = fragmentManager
        mLoadingDialog = LoadingDialog.newInstance()
    }

    constructor(fragmentManager: FragmentManager, isShow: Boolean) {
        this.mFragmentManager = fragmentManager
        this.isShowLoading = isShow
        mLoadingDialog = LoadingDialog.newInstance()
    }


    override fun onSubscribe(d: Disposable) {
        if (isShowLoading) {
            showLoading()
        }
    }

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
        if (isShowLoading) {
            hideLoading()
            onFailure(ExceptionUtils.handleException(e))
        }
    }

    protected fun onFailure(errorMsg: String) {
        ToastUtils.showShort(errorMsg)
    }

    override fun onComplete() {
        if (isShowLoading) {
            hideLoading()
        }
    }

    private fun showLoading() {
        mLoadingDialog.showDialog(mFragmentManager)
    }

    private fun hideLoading() {
        mLoadingDialog.dismissDialog()
    }

    abstract fun onSuccess(result: T?)
}
