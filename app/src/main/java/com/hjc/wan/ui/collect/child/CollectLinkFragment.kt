package com.hjc.wan.ui.collect.child

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.collect.adapter.CollectLinkAdapter
import com.hjc.wan.ui.collect.contract.CollectLinkContract
import com.hjc.wan.ui.collect.presenter.CollectLinkPresenter
import com.hjc.wan.utils.helper.RouterManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_collect_link.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:40
 * @Description: 收藏网址页面
 */
class CollectLinkFragment :
    BaseMvpLazyFragment<CollectLinkContract.View, CollectLinkPresenter>(),
    CollectLinkContract.View {

    private lateinit var mAdapter: CollectLinkAdapter


    companion object {

        fun newInstance(): CollectLinkFragment {
            return CollectLinkFragment()
        }
    }

    override fun createPresenter(): CollectLinkPresenter {
        return CollectLinkPresenter()
    }

    override fun createView(): CollectLinkContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_link
    }

    override fun initView() {
        super.initView()
        val manager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        rvCollectLink.layoutManager = manager

        mAdapter = CollectLinkAdapter(null)
        rvCollectLink.adapter = mAdapter
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter()?.loadListData()
    }

    override fun showList(result: MutableList<CollectLinkBean>) {
        mAdapter.setNewData(result)
    }

    override fun addListeners() {
        super.addListeners()

        mAdapter.setOnItemClickListener { _, _, position ->
            val dataList = mAdapter.data
            val bean = dataList[position]

            RouterManager.jumpToWeb(bean.name, bean.link)
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val dataList = mAdapter.data
            val bean = dataList[position]

        }

        smartRefreshLayout.setOnRefreshListener {
            getPresenter()?.loadListData()
        }
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