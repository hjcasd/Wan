package com.hjc.wan.ui.project.child

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.ui.project.adapter.ProjectChildAdapter
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.project.contract.ProjectChildPresenter
import com.hjc.wan.ui.project.presenter.ProjectChildContract
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_project_child.*
import java.util.concurrent.TimeUnit


/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 子项目页面
 */
class ProjectChildFragment : BaseMvpLazyFragment<ProjectChildContract.View, ProjectChildPresenter>(),
    ProjectChildContract.View {

    private var cid: Int = 0

    private var mPage: Int = 0

    private lateinit var mProjectChildAdapter: ProjectChildAdapter

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

        mProjectChildAdapter = ProjectChildAdapter(null)
        rvProject.adapter = mProjectChildAdapter
    }

    override fun initData() {
        super.initData()

        cid = arguments?.get("cid") as Int

        showLoading()
        getPresenter().loadListData(mPage, cid)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mProjectChildAdapter.setNewData(result)
        } else {
            mProjectChildAdapter.addData(result)
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

        mProjectChildAdapter.setOnCollectViewClickListener(object :
            ProjectChildAdapter.OnCollectViewClickListener {

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
