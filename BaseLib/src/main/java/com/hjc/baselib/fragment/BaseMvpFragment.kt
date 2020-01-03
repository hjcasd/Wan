package com.hjc.baselib.fragment

import android.os.Bundle
import com.hjc.baselib.base.BasePresenter
import com.hjc.baselib.base.IBaseView

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:50
 * @Description: Fragment基类(MVP)
 */
abstract class BaseMvpFragment<V : IBaseView, P : BasePresenter<V>?> : BaseFragment() {

    private var mPresenter: P? = null
    private lateinit var mView: V


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        mPresenter = createPresenter()
        mView = createView()
        mPresenter?.attachView(mView)
    }

    abstract fun createPresenter(): P

    abstract fun createView(): V

    protected fun getPresenter(): P? {
        return mPresenter
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}
