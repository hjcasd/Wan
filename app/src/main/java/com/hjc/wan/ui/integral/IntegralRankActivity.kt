package com.hjc.wan.ui.integral

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.widget.bar.OnViewClickListener
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.integral.adapter.IntegralRankAdapter
import com.hjc.wan.ui.integral.contract.IntegralRankContract
import com.hjc.wan.ui.integral.presenter.IntegralRankPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_integral_rank.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分排行页面
 */
@Route(path = RoutePath.URL_INTEGRAL_RANK)
class IntegralRankActivity : BaseMvpActivity<IntegralRankContract.View, IntegralRankPresenter>(),
    IntegralRankContract.View {

    private lateinit var mIntegralRankAdapter: IntegralRankAdapter

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

        val manager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvIntegralRank.layoutManager = manager

        mIntegralRankAdapter = IntegralRankAdapter(null)
        rvIntegralRank.adapter = mIntegralRankAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<IntegralBean>) {
        if (mPage == 1) {
            mIntegralRankAdapter.setNewData(result)
        } else {
            mIntegralRankAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()
        fabHistory.setOnClickListener(this)

        titleBar.setOnViewClickListener(object : OnViewClickListener {

            override fun leftClick(view: View?) {
                finish()
            }

            override fun rightClick(view: View?) {
                RouterManager.jumpToWeb("积分规则", "https://www.wanandroid.com/blog/show/2653")
            }

        })

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