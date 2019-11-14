package com.hjc.wan.ui.fragment.square.child

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.ui.contract.square.SystemContract
import com.hjc.wan.ui.fragment.square.adapter.SystemAdapter
import com.hjc.wan.ui.model.SystemBean
import com.hjc.wan.ui.presenter.square.SystemPresenter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_system.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 体系子页面
 */
class SystemFragment : BaseMvpLazyFragment<SystemContract.View, SystemPresenter>(),
    SystemContract.View {

    private lateinit var mSystemAdapter: SystemAdapter

    companion object {

        fun newInstance(): SystemFragment {
            return SystemFragment()
        }
    }

    override fun createPresenter(): SystemPresenter {
        return SystemPresenter()
    }

    override fun createView(): SystemContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvSystem.layoutManager = manager

        mSystemAdapter = SystemAdapter(null)
        rvSystem.adapter = mSystemAdapter
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter().loadListData()
    }

    override fun showList(result: MutableList<SystemBean>) {
        mSystemAdapter.setNewData(result)
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshListener { getPresenter().loadListData() }
    }

    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                stateView.showContent()
                smartRefreshLayout.finishRefresh()
            }
    }

    override fun showLoading() {
        super.showLoading()
        stateView.showLoading()
    }

    override fun showError() {
        stateView.showError()
        smartRefreshLayout.finishRefresh()
    }

    override fun showEmpty() {
        stateView.showEmpty()
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoNetwork() {
        stateView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
    }

}