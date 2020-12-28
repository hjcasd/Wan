package com.hjc.baselib.fragment

import android.os.Bundle
import android.view.View
import com.hjc.baselib.base.BasePresenter
import com.hjc.baselib.base.IBaseView
import com.hjc.baselib.loadsir.EmptyCallback
import com.hjc.baselib.loadsir.ErrorCallback
import com.hjc.baselib.loadsir.LoadingCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:50
 * @Description: Fragment基类(MVP)
 */
abstract class BaseMvpFragment<V : IBaseView, P : BasePresenter<V>?> : BaseFragment() {

    private var mPresenter: P? = null
    private lateinit var mView: V

    protected var mLoadService: LoadService<*>? = null

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mView = createView()
        mPresenter?.attachView(mView)
    }

    abstract fun createPresenter(): P

    abstract fun createView(): V

    protected fun getPresenter(): P? {
        return mPresenter
    }

    /**
     * 注册LoadSir
     *
     * @param view 状态视图
     */
    protected open fun initLoadSir(view: View?) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault().register(view) { v: View? -> this.onRetryBtnClick(v) }
        }
    }

    override fun showContent() {
        mLoadService?.showSuccess()
    }

    override fun showLoading() {
        mLoadService?.showCallback(LoadingCallback::class.java)
    }

    override fun showEmpty() {
        mLoadService?.showCallback(EmptyCallback::class.java)
    }

    override fun showError() {
        mLoadService?.showCallback(ErrorCallback::class.java)
    }

    /**
     * 失败重试,重新加载事件
     */
    protected open fun onRetryBtnClick(v: View?) {}

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}
