package com.hjc.wan.ui.todo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.widget.bar.OnBarClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityTodoListBinding
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.adapter.TodoAdapter
import com.hjc.wan.ui.todo.contract.TodoContract
import com.hjc.wan.ui.todo.presenter.TodoPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.dialog.OperateDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author: HJC
 * @Date: 2019/12/24 11:03
 * @Description: 待办清单页面
 */
@Route(path = RoutePath.URL_TO_DO)
class TodoListActivity :
    BaseActivity<ActivityTodoListBinding, TodoContract.View, TodoPresenter>(),
    TodoContract.View {

    private lateinit var mAdapter: TodoAdapter

    private var mPage = 0

    override fun createPresenter(): TodoPresenter {
        return TodoPresenter()
    }

    override fun createView(): TodoContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        initLoadSir(mBinding.refreshLayout)

        val manager = LinearLayoutManager(this)
        mBinding.rvList.layoutManager = manager

        mAdapter = TodoAdapter(null)
        mBinding.rvList.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }

        mBinding.titleBar.setBgColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        EventManager.register(this)
        getPresenter().loadListData(mPage, true)
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
        getPresenter().loadListData(mPage, false)
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishRefresh()
        mBinding.refreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        mBinding.titleBar.setOnBarClickListener(object : OnBarClickListener {

            override fun leftClick(view: View) {
                finish()
            }

            override fun rightClick(view: View) {
                val bundle = Bundle()
                bundle.putInt("from", 0)
                RouterManager.jumpWithCode(
                    this@TodoListActivity,
                    RoutePath.URL_ADD_TO_DO,
                    bundle,
                    100
                )
            }
        })

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter().loadListData(mPage, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage, false)
            }

        })

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]

                val bundle = Bundle()
                bundle.putInt("from", 1)
                bundle.putSerializable("bean", bean)
                RouterManager.jumpWithCode(
                    this@TodoListActivity,
                    RoutePath.URL_ADD_TO_DO,
                    bundle,
                    100
                )
            }

            setOnItemChildClickListener { _, _, position ->
                val bean = mAdapter.data[position]

                OperateDialog.newInstance(bean.id, bean.isDone())
                    .setAnimStyle(R.style.dialog_anim_bottom)
                    .showDialog(supportFragmentManager)
            }
        }
    }

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        mPage = 1
        getPresenter().loadListData(mPage, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.DELETE_TODO) {
            getPresenter().deleteTodo(event.data as Int)
        } else if (event?.code == EventCode.DONE_TODO) {
            getPresenter().finishTodo(event.data as Int)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 1000) {
            mPage = 1
            getPresenter().loadListData(mPage, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }

}