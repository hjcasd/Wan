package com.hjc.wan.ui.square.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.square.adapter.SystemTagAdapter
import com.hjc.wan.ui.square.contract.SystemTagContract
import com.hjc.wan.ui.square.presenter.SystemTagPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_system_tag.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/11 16:38
 * @Description: 体系tag下的文章列表页面
 */
@Route(path = RoutePath.URL_SYSTEM_TAG)
class SystemTagActivity : BaseMvpActivity<SystemTagContract.View, SystemTagPresenter>(),
    SystemTagContract.View {

    private lateinit var mSystemTagAdapter: SystemTagAdapter

    private var articleList: MutableList<ArticleBean> = mutableListOf()

    @Autowired(name = "params")
    @JvmField
    var bundle: Bundle? = null

    private var mCid = 0

    private var mPage = 0

    override fun createPresenter(): SystemTagPresenter {
        return SystemTagPresenter()
    }

    override fun createView(): SystemTagContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_system_tag
    }

    override fun initView() {
        super.initView()
        val manager = LinearLayoutManager(this)
        rvTag.layoutManager = manager

        mSystemTagAdapter = SystemTagAdapter(null)
        rvTag.adapter = mSystemTagAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        bundle?.let {
            val title = it.getString("title")
            val cid = it.getInt("id", 0)

            LogUtils.e("id: " + cid)

            titleBar.setTitle(title)
            mCid = cid

            showLoading()
            getPresenter().loadListData(mPage, mCid)
        }
    }


    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            articleList = result
            mSystemTagAdapter.setNewData(result)
        } else {
            articleList.addAll(result)
            mSystemTagAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        titleBar.setOnViewLeftClickListener { finish() }

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter().loadListData(mPage, mCid)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage, mCid)
            }

        })

        mSystemTagAdapter.setOnItemClickListener { _, _, position ->
            val bean = articleList[position]
            RouterManager.jumpToWeb(bean.title, bean.link)
        }

        mSystemTagAdapter.setOnCollectViewClickListener(object :
            SystemTagAdapter.OnCollectViewClickListener {

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
        mSystemTagAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mSystemTagAdapter.notifyDataSetChanged()
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