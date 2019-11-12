package com.hjc.wan.base

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.hjc.baselib.fragment.BaseLazyFragment

/**
 * @Author: HJC
 * @Date: 2019/1/4 15:02
 * @Description: Fragment基类(mvp)
 */
abstract class BaseMvpLazyFragment<V : IBaseView, P : BasePresenter<V>> : BaseLazyFragment(), View.OnClickListener {
    private lateinit var mPresenter: P
    private lateinit var mView: V

    override fun initData() {
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

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
