package com.hjc.wan.ui.integral.child

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.IntegralHistoryBean
import com.hjc.wan.ui.integral.adapter.IntegralHistoryAdapter
import com.hjc.wan.ui.integral.contract.IntegralHistoryContract
import com.hjc.wan.ui.integral.presenter.IntegralHistoryPresenter
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_integral_history.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    private lateinit var mAdapter: IntegralHistoryAdapter

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

        mAdapter = IntegralHistoryAdapter(null)
        rvIntegralHistory.adapter = mAdapter

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        EventManager.register(this)

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

        titleBar.setOnViewLeftClickListener { finish() }

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

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<Any>) {
        if (event.code == EventCode.CHANGE_LIST_ANIMATION) {
            if (SettingManager.getListAnimationType() != 0) {
                mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

}