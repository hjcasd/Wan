package com.hjc.wan.ui.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.widget.bar.OnViewClickListener
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.adapter.TodoAdapter
import com.hjc.wan.ui.todo.contract.TodoContract
import com.hjc.wan.ui.todo.presenter.TodoPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_todo.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/12/24 11:03
 * @Description: 待办清单页面
 */
@Route(path = RoutePath.URL_TO_DO)
class TodoActivity : BaseMvpActivity<TodoContract.View, TodoPresenter>(), TodoContract.View{

    private lateinit var mAdapter: TodoAdapter

    private var mPage = 0

    override fun createPresenter(): TodoPresenter {
        return  TodoPresenter()
    }

    override fun createView(): TodoContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_todo
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvTodo.layoutManager = manager

        mAdapter = TodoAdapter(null)
        rvTodo.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<TodoBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        titleBar.setOnViewClickListener(object : OnViewClickListener {

            override fun leftClick(view: View?) {
                finish()
            }

            override fun rightClick(view: View?) {
                val bundle = Bundle()
                bundle.putInt("from", 0)
                RouterManager.jumpWithCode(this@TodoActivity, RoutePath.URL_ADD_TO_DO, bundle, 100)
            }

        })

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter()?.loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 1000){
            mPage = 1
            getPresenter()?.loadListData(mPage)
        }
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