package com.hjc.wan.ui.integral

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpListActivity
import com.hjc.baselib.widget.bar.OnBarRightClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.integral.adapter.IntegralRankAdapter
import com.hjc.wan.ui.integral.contract.IntegralRankContract
import com.hjc.wan.ui.integral.presenter.IntegralRankPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_integral_rank.*

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分排行页面
 */
@Route(path = RoutePath.URL_INTEGRAL_RANK)
class IntegralRankActivity : BaseMvpListActivity<IntegralRankContract.View, IntegralRankPresenter>(),
    IntegralRankContract.View {

    private lateinit var mAdapter: IntegralRankAdapter

    private var mPage: Int = 1

    override fun createPresenter(): IntegralRankPresenter {
        return IntegralRankPresenter()
    }

    override fun createView(): IntegralRankContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_integral_rank
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvIntegralRank.layoutManager = manager

        mAdapter = IntegralRankAdapter(null)
        rvIntegralRank.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }

        fabHistory.backgroundTintList = SettingManager.getStateList()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
            .init()
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.setTitle("积分排行")
        titleBar.setRightImage(R.mipmap.icon_rule)
        titleBar.setBgColor(SettingManager.getThemeColor())
        titleBar.setOnBarRightClickListener(object : OnBarRightClickListener {

            override fun rightClick(view: View?) {
                RouterManager.jumpToWeb("积分规则", "https://www.wanandroid.com/blog/show/2653")
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<IntegralBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()
        fabHistory.setOnClickListener(this)

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

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)
        when (v?.id) {
            R.id.fabHistory -> RouterManager.jump(RoutePath.URL_INTEGRAL_HISTORY)
        }
    }
}