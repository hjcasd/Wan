package com.hjc.wan.ui.publics.child

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.publics.adapter.PublicChildAdapter
import com.hjc.wan.ui.publics.contract.PublicChildContract
import com.hjc.wan.ui.publics.presenter.PublicChildPresenter
import com.hjc.wan.utils.helper.RouterManager
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

    private lateinit var mPublicChildAdapter: PublicChildAdapter

    private var articleList: MutableList<ArticleBean> = mutableListOf()

    private var cid: Int = 0

    private var mPage: Int = 1


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
            articleList = result
            mPublicChildAdapter.setNewData(result)
        } else {
            articleList.addAll(result)
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

        mPublicChildAdapter.setOnItemClickListener { _, _, position ->
            val bean = articleList[position]
            RouterManager.jumpToWeb(bean.title, bean.link)
        }

        mPublicChildAdapter.setOnCollectViewClickListener(object :
            PublicChildAdapter.OnCollectViewClickListener {

            override fun onClick(checkBox: CheckBox, position: Int) {
                val bean = articleList[position]
                if (!bean.collect) {
                    getPresenter().collectArticle(bean)
                } else {
                    getPresenter().unCollectArticle(bean)
                }
            }
        })
    }


    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mPublicChildAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mPublicChildAdapter.notifyDataSetChanged()
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