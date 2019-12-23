package com.hjc.wan.base

import android.os.Bundle
import android.view.View
import com.hjc.baselib.activity.BaseActivity

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:00
 * @Description: Activity基类(mvp)
 */
abstract class BaseMvpActivity<V : IBaseView, P : BasePresenter<V>?> : BaseActivity(), IBaseView {

    private var mPresenter: P? = null
    private lateinit var mView: V

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mView = createView()
        mPresenter?.attachView(mView)
    }

    abstract fun createPresenter(): P

    abstract fun createView(): V

    fun getPresenter(): P? {
        return mPresenter
    }

    override fun initView() {

    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    override fun onDestroy() {
        mPresenter?.detachView()
        super.onDestroy()
    }
}
