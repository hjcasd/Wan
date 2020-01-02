package com.hjc.wan.ui.collect.child

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.CollectArticleBean
import com.hjc.wan.ui.collect.adapter.CollectArticleAdapter
import com.hjc.wan.ui.collect.contract.CollectArticleContract
import com.hjc.wan.ui.collect.presenter.CollectArticlePresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_collect_article.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:40
 * @Description: 收藏文章页面
 */
class CollectArticleFragment :
    BaseMvpLazyFragment<CollectArticleContract.View, CollectArticlePresenter>(),
    CollectArticleContract.View {

    private lateinit var mAdapter: CollectArticleAdapter

    private var mPage: Int = 0


    companion object {

        fun newInstance(): CollectArticleFragment {
            return CollectArticleFragment()
        }
    }

    override fun createPresenter(): CollectArticlePresenter {
        return CollectArticlePresenter()
    }

    override fun createView(): CollectArticleContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_article
    }

    override fun initView() {
        super.initView()
        val manager = LinearLayoutManager(mContext)
        rvCollectArticle.layoutManager = manager

        mAdapter = CollectArticleAdapter(null)
        rvCollectArticle.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<CollectArticleBean>) {
        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter()?.loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
            }

        })

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean =  mAdapter.data[position]

                RouterManager.jumpToWeb(bean.title, bean.link)
            }

            setOnItemChildClickListener { _, _, position ->
                val bean =  mAdapter.data[position]

                getPresenter()?.unCollectArticle(bean, position)
            }
        }
    }

    override fun showUnCollectList(position: Int) {
        mAdapter.remove(position)
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