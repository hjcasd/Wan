package com.hjc.wan.ui.fragment.square.child

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.ui.contract.square.PlazaContract
import com.hjc.wan.ui.fragment.square.adapter.PlazaAdapter
import com.hjc.wan.ui.model.ArticleBean
import com.hjc.wan.ui.presenter.square.PlazaPresenter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_plaza.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 广场子页面
 */
class PlazaFragment : BaseMvpLazyFragment<PlazaContract.View, PlazaPresenter>(),
    PlazaContract.View {

    private var mPage = 0

    private lateinit var mPlazaAdapter: PlazaAdapter

    companion object {

        fun newInstance(): PlazaFragment {
            return PlazaFragment()
        }
    }

    override fun createPresenter(): PlazaPresenter {
        return PlazaPresenter()
    }

    override fun createView(): PlazaContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_plaza
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvPlaza.layoutManager = manager

        mPlazaAdapter = PlazaAdapter(null)
        rvPlaza.adapter = mPlazaAdapter
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter().loadListData(mPage)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mPlazaAdapter.setNewData(result)
        } else {
            mPlazaAdapter.addData(result)
        }
    }


    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter().loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage)
            }

        })

        mPlazaAdapter.setOnCollectViewClickListener(object :
            PlazaAdapter.OnCollectViewClickListener {

            override fun onClick(checkBox: CheckBox, position: Int) {
                if (checkBox.isChecked) {
                    ToastUtils.showShort("选中了: $position")
                } else {
                    ToastUtils.showShort("未选中: $position")
                }
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
        super.showLoading()
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