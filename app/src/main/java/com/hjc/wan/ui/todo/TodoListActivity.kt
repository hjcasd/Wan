package com.hjc.wan.ui.todo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpListActivity
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.widget.bar.OnBarRightClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.adapter.TodoAdapter
import com.hjc.wan.ui.todo.contract.TodoContract
import com.hjc.wan.ui.todo.presenter.TodoPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.dialog.OperateDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_common.*

/**
 * @Author: HJC
 * @Date: 2019/12/24 11:03
 * @Description: 待办清单页面
 */
@Route(path = RoutePath.URL_TO_DO)
class TodoListActivity : BaseMvpListActivity<TodoContract.View, TodoPresenter>(), TodoContract.View {

    private lateinit var mAdapter: TodoAdapter

    private var mPage = 0

    override fun createPresenter(): TodoPresenter {
        return TodoPresenter()
    }

    override fun createView(): TodoContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_common
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvCommon.layoutManager = manager

        mAdapter = TodoAdapter(null)
        rvCommon.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
            .init()
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.setTitle("待办清单")
        titleBar.setRightImage(R.mipmap.icon_add)
        titleBar.setBgColor(SettingManager.getThemeColor())
        titleBar.setOnBarRightClickListener(object : OnBarRightClickListener {

            override fun rightClick(view: View?) {
                val bundle = Bundle()
                bundle.putInt("from", 0)
                RouterManager.jumpWithCode(this@TodoListActivity, RoutePath.URL_ADD_TO_DO, bundle, 100)
            }
        })
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
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

    override fun refreshList() {
        mPage = 1
        getPresenter()?.loadListData(mPage)
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
            }

        })

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]

                val bundle = Bundle()
                bundle.putInt("from", 1)
                bundle.putSerializable("bean", bean)
                RouterManager.jumpWithCode(this@TodoListActivity, RoutePath.URL_ADD_TO_DO, bundle, 100)
            }

            setOnItemChildClickListener { _, _, position ->
                val bean =  mAdapter.data[position]

                OperateDialog.newInstance(bean.id, bean.isDone())
                    .setAnimStyle(R.style.dialog_anim_bottom)
                    .showDialog(supportFragmentManager)
            }
        }
    }

    override fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.DELETE_TODO) {
            getPresenter()?.deleteTodo(event.data as Int)
        } else if (event?.code == EventCode.DONE_TODO) {
            getPresenter()?.finishTodo(event.data as Int)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 1000) {
            refreshList()
        }
    }

}