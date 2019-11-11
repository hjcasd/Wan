package com.hjc.wan.base

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.hjc.baselib.activity.BaseActivity

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:00
 * @Description: Activity基类(mvp)
 */
abstract class BaseMvpActivity<V : IBaseView, P : BasePresenter<V>> : BaseActivity() {
    private lateinit var mPresenter: P
    private lateinit var mView: V

    override fun initData(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)

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

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}
