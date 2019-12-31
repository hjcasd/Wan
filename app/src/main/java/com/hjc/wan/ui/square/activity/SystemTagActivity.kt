package com.hjc.wan.ui.square.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.square.adapter.SystemTagAdapter
import com.hjc.wan.ui.square.contract.SystemTagContract
import com.hjc.wan.ui.square.presenter.SystemTagPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_system_tag.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/11 16:38
 * @Description: 体系tag下的文章列表页面
 */
@Route(path = RoutePath.URL_SYSTEM_TAG)
class SystemTagActivity : BaseMvpActivity<SystemTagContract.View, SystemTagPresenter>(),
    SystemTagContract.View {

    private lateinit var mAdapter: SystemTagAdapter

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

        mAdapter = SystemTagAdapter(null)
        rvTag.adapter = mAdapter

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        EventManager.register(this)

        bundle?.let {
            val title = it.getString("title")
            val cid = it.getInt("id", 0)

            titleBar.setTitle(title)
            mCid = cid

            showLoading()
            getPresenter()?.loadListData(mPage, mCid)
        }
    }


    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
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
                mPage = 0
                getPresenter()?.loadListData(mPage, mCid)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage, mCid)
            }

        })

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                RouterManager.jumpToWeb(bean.title, bean.link)
            }

            setOnItemChildClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                if (!bean.collect) {
                    getPresenter()?.collectArticle(bean)
                } else {
                    getPresenter()?.unCollectArticle(bean)
                }
            }
        }
    }


    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mAdapter.notifyDataSetChanged()
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