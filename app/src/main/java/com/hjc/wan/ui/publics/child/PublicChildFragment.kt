package com.hjc.wan.ui.publics.child

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.publics.adapter.PublicChildAdapter
import com.hjc.wan.ui.publics.contract.PublicChildContract
import com.hjc.wan.ui.publics.presenter.PublicChildPresenter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_public_child.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/18 10:54
 * @Description: 公众号子页面
 */
class PublicChildFragment : BaseMvpLazyFragment<PublicChildContract.View, PublicChildPresenter>(),
    PublicChildContract.View {

    private var cid: Int = 0

    private var mPage: Int = 1

    private lateinit var mPublicChildAdapter: PublicChildAdapter

    companion object {

        fun newInstance(cid: Int): PublicChildFragment {
            val publicChildFragment= PublicChildFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            publicChildFragment.arguments = bundle
            return publicChildFragment
        }
    }

    override fun createPresenter(): PublicChildPresenter {
        return PublicChildPresenter()
    }

    override fun createView(): PublicChildContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_public_child
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvPublic.layoutManager = manager

        mPublicChildAdapter = PublicChildAdapter(null)
        rvPublic.adapter = mPublicChildAdapter
    }

    override fun initData() {
        super.initData()

        cid = arguments?.getInt("cid") ?: 0

        showLoading()
        getPresenter().loadListData(mPage, cid)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mPublicChildAdapter.setNewData(result)
        } else {
            mPublicChildAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter().loadListData(mPage, cid)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage, cid)
            }

        })

        mPublicChildAdapter.setOnCollectViewClickListener(object :
            PublicChildAdapter.OnCollectViewClickListener {

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