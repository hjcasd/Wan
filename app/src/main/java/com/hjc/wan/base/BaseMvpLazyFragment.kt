package com.hjc.wan.base

import android.view.View
import com.hjc.baselib.fragment.BaseLazyFragment

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:02
 * @Description: Fragment基类(mvp)
 */
abstract class BaseMvpLazyFragment<V : IBaseView, P : BasePresenter<V>?> : BaseLazyFragment(),
    IBaseView {

    private var mPresenter: P? = null
    private lateinit var mView: V

    override fun initData() {
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

    override fun onDestroyView() {
        mPresenter?.detachView()
        super.onDestroyView()
    }
}
