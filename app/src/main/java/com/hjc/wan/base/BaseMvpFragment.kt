package com.hjc.wan.base

import android.os.Bundle
import android.view.View
import com.hjc.baselib.fragment.BaseFragment

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:02
 * @Description: Fragment基类(mvp)
 */
abstract class BaseMvpFragment<V : IBaseView, P : BasePresenter<V>> : BaseFragment(), View.OnClickListener {
    private lateinit var mPresenter: P
    private lateinit var mView: V

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mView = createView()
        mPresenter.attachView(mView)
    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    abstract fun createPresenter(): P

    abstract fun createView(): V

    fun getPresenter(): P {
        return mPresenter
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
