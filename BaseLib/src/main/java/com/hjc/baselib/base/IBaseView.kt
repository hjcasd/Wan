package com.hjc.baselib.base

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:03
 * @Description: View层基类
 */
interface IBaseView {

    /**
     * 显示loading
     */
    fun startLoading()

    /**
     * 显示内容
     */
    fun dismissLoading()

    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示loading
     */
    fun showLoading()

    /**
     * 显示空页面
     */
    fun showEmpty()

    /**
     * 加载失败
     */
    fun showError()
}
