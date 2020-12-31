package com.hjc.wan.ui.integral

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.baselib.widget.bar.OnBarLeftClickListener
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityIntegralHistoryBinding
import com.hjc.wan.model.IntegralHistoryBean
import com.hjc.wan.ui.integral.adapter.IntegralHistoryAdapter
import com.hjc.wan.ui.integral.contract.IntegralHistoryContract
import com.hjc.wan.ui.integral.presenter.IntegralHistoryPresenter
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分记录页面
 */
@Route(path = RoutePath.URL_INTEGRAL_HISTORY)
class IntegralHistoryActivity :
    BaseActivity<ActivityIntegralHistoryBinding, IntegralHistoryContract.View, IntegralHistoryPresenter>(),
    IntegralHistoryContract.View {

    private lateinit var mAdapter: IntegralHistoryAdapter

    private var mPage: Int = 1

    override fun createPresenter(): IntegralHistoryPresenter {
        return IntegralHistoryPresenter()
    }

    override fun createView(): IntegralHistoryContract.View {
        return this
    }


    override fun initView() {
        super.initView()

        initLoadSir(mBinding.refreshLayout)

        val manager = LinearLayoutManager(this)
        mBinding.rvHistory.layoutManager = manager

        mAdapter = IntegralHistoryAdapter(null)
        mBinding.rvHistory.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }

        mBinding.titleBar.setBgColor(SettingManager.getThemeColor())
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        getPresenter().loadListData(mPage, true)
    }

    override fun showList(result: MutableList<IntegralHistoryBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishRefresh()
        mBinding.refreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        mBinding.titleBar.setOnBarLeftClickListener(object : OnBarLeftClickListener {

            override fun leftClick(view: View) {
                finish()
            }
        })

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter().loadListData(mPage, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage, false)
            }

        })
    }

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        mPage = 1
        getPresenter().loadListData(mPage, true)
    }
}