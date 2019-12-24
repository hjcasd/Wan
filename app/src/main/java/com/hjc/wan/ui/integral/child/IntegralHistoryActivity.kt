package com.hjc.wan.ui.integral.child

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.IntegralHistoryBean
import com.hjc.wan.ui.integral.adapter.IntegralHistoryAdapter
import com.hjc.wan.ui.integral.contract.IntegralHistoryContract
import com.hjc.wan.ui.integral.presenter.IntegralHistoryPresenter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_integral_history.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分记录页面
 */
@Route(path = RoutePath.URL_INTEGRAL_HISTORY)
class IntegralHistoryActivity :
    BaseMvpActivity<IntegralHistoryContract.View, IntegralHistoryPresenter>(),
    IntegralHistoryContract.View {

    private lateinit var mIntegralHistoryAdapter: IntegralHistoryAdapter

    private var mPage: Int = 1

    override fun createPresenter(): IntegralHistoryPresenter {
        return IntegralHistoryPresenter()
    }

    override fun createView(): IntegralHistoryContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_integral_history
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvIntegralHistory.layoutManager = manager

        mIntegralHistoryAdapter = IntegralHistoryAdapter(null)
        rvIntegralHistory.adapter = mIntegralHistoryAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<IntegralHistoryBean>) {
        if (mPage == 1) {
            mIntegralHistoryAdapter.setNewData(result)
        } else {
            mIntegralHistoryAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        titleBar.setOnViewLeftClickListener { _ -> finish() }

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


    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                stateView.showContent()
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.setEnableLoadMore(true)
            }
    }

    override fun showLoading() {
        stateView.showLoading()
    }

    override fun showError() {
        stateView.showError()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showEmpty() {
        stateView.showEmpty()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showNoNetwork() {
        stateView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

}