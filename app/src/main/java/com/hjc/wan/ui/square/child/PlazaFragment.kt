package com.hjc.wan.ui.square.child

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.square.adapter.PlazaAdapter
import com.hjc.wan.ui.square.contract.PlazaContract
import com.hjc.wan.ui.square.presenter.PlazaPresenter
import com.hjc.wan.utils.helper.RouterManager
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

    private lateinit var mPlazaAdapter: PlazaAdapter

    private var articleList: MutableList<ArticleBean> = mutableListOf()

    private var mPage = 0


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
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            articleList = result
            mPlazaAdapter.setNewData(result)
        } else {
            articleList.addAll(result)
            mPlazaAdapter.addData(result)
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

        mPlazaAdapter.setOnItemClickListener { _, _, position ->
            val bean = articleList[position]
            RouterManager.jumpToWeb(bean.title, bean.link)
        }

        mPlazaAdapter.setOnCollectViewClickListener(object :
            PlazaAdapter.OnCollectViewClickListener {

            override fun onClick(checkBox: CheckBox, position: Int) {
                val bean = articleList[position]
                if (!bean.collect) {
                    getPresenter()?.collectArticle(bean)
                } else {
                    getPresenter()?.unCollectArticle(bean)
                }
            }
        })
    }


    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mPlazaAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mPlazaAdapter.notifyDataSetChanged()
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