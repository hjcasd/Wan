package com.hjc.wan.ui.integral.child

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpListActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.IntegralHistoryBean
import com.hjc.wan.ui.integral.adapter.IntegralHistoryAdapter
import com.hjc.wan.ui.integral.contract.IntegralHistoryContract
import com.hjc.wan.ui.integral.presenter.IntegralHistoryPresenter
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_common.*

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分记录页面
 */
@Route(path = RoutePath.URL_INTEGRAL_HISTORY)
class IntegralHistoryActivity :
    BaseMvpListActivity<IntegralHistoryContract.View, IntegralHistoryPresenter>(),
    IntegralHistoryContract.View {

    private lateinit var mAdapter: IntegralHistoryAdapter

    private var mPage: Int = 1

    override fun createPresenter(): IntegralHistoryPresenter {
        return IntegralHistoryPresenter()
    }

    override fun createView(): IntegralHistoryContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_common
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvCommon.layoutManager = manager

        mAdapter = IntegralHistoryAdapter(null)
        rvCommon.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
            .init()
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.setTitle("积分记录")
        titleBar.setBgColor(SettingManager.getThemeColor())
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<IntegralHistoryBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter()?.loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
            }

        })
    }
}