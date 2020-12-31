package com.hjc.wan.common

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.baselib.base.IBaseView
import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.http.exception.ApiException
import com.hjc.wan.http.exception.ExceptionUtils
import com.hjc.wan.http.exception.ServerCode
import kotlinx.coroutines.*

/**
 * @Author: HJC
 * @Date: 2020/12/28 9:27
 * @Description: 协程网络请求封装presenter
 */
abstract class KotlinPresenter<V : IBaseView> : BasePresenter<V>() {

    protected var mainScope = MainScope()

    /**
     * 请求处理(response封装)
     */
    protected fun <T> launchWrapper(
        api: suspend CoroutineScope.() -> BaseResponse<T>,
        success: CoroutineScope.(T?) -> Unit,
        isShowLoading: Boolean = false,
        isShowStatus: Boolean = false,
        error: (e: Throwable) -> Unit = {},
    ) {
        if (isShowLoading) {
            getView()?.startLoading()
        }
        if (isShowStatus) {
            getView()?.showLoading()
        }
        mainScope.launch {
            try {
                withContext(Dispatchers.IO) { //异步请求接口
                    val response = api()
                    withContext(Dispatchers.Main) {
                        if (ServerCode.CODE_SUCCESS == response.errorCode) { //请求成功
                            success(response.data)
                        } else { //请求成功,Code错误,抛出ApiException
                            handleError(ApiException(response.errorMsg, response.errorCode))
                            error(ApiException(response.errorMsg, response.errorCode))
                        }
                    }
                }
            } catch (e: Throwable) { //请求失败
                handleError(e)
                error(e)
            } finally { //请求结束
                if (isShowLoading) {
                    getView()?.dismissLoading()
                }
            }
        }
    }

    /**
     * 请求处理(response未封装)
     */
    protected fun <T> launchOriginal(
        api: suspend CoroutineScope.() -> T,
        success: CoroutineScope.(T?) -> Unit,
        isShowLoading: Boolean = false
    ) {
        if (isShowLoading) {
            getView()?.startLoading()
        }
        mainScope.launch {
            try {
                withContext(Dispatchers.IO) { //异步请求接口
                    val response = api()
                    withContext(Dispatchers.Main) {
                        success(response)
                    }
                }
            } catch (e: Throwable) { //请求失败
                handleError(e)
            } finally { //请求结束
                if (isShowLoading) {
                    getView()?.dismissLoading()
                }
            }
        }
    }

    /**
     * 错误统一处理
     */
    private fun handleError(e: Throwable) {
        val errorDesc = ExceptionUtils.handleException(e)
        ToastUtils.showShort(errorDesc)
    }

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }
}