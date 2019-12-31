package com.hjc.wan.ui.integral

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.widget.bar.OnViewClickListener
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.integral.adapter.IntegralRankAdapter
import com.hjc.wan.ui.integral.contract.IntegralRankContract
import com.hjc.wan.ui.integral.presenter.IntegralRankPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_integral_rank.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/23 10:05
 * @Description: 积分排行页面
 */
@Route(path = RoutePath.URL_INTEGRAL_RANK)
class IntegralRankActivity : BaseMvpActivity<IntegralRankContract.View, IntegralRankPresenter>(),
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