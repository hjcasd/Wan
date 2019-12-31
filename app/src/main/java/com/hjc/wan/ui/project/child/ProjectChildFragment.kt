package com.hjc.wan.ui.project.child

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.project.adapter.ProjectChildAdapter
import com.hjc.wan.ui.project.contract.ProjectChildContract
import com.hjc.wan.ui.project.presenter.ProjectChildPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_project_child.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit


/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 项目子页面
 */
class ProjectChildFragment : BaseMvpLazyFragment<ProjectChildContract.View, ProjectChildPresenter>(),
    ProjectChildContract.View {

    private lateinit var mAdapter: ProjectChildAdapter

    private var cid: Int = 0

    private var mPage: Int = 1


    companion object {

        fun newInstance(cid: Int): ProjectChildFragment {
            val projectChildFragment= ProjectChildFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            projectChildFragment.arguments = bundle
            return projectChildFragment
        }
    }

    override fun createPresenter(): ProjectChildPresenter {
        return ProjectChildPresenter()
    }

    override fun createView(): ProjectChildContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvProject.layoutManager = manager

        mAdapter = ProjectChildAdapter(null)
        rvProject.adapter = mAdapter

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
        }
    }

    override fun initData() {
        super.initData()
        EventManager.register(this)

        cid = arguments?.getInt("cid") ?: 0

        showLoading()
        getPresenter()?.loadListData(mPage, cid)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter()?.loadListData(mPage, cid)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage, cid)
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
