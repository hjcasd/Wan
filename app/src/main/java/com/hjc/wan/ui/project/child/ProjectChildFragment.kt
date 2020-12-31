package com.hjc.wan.ui.project.child

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.databinding.FragmentCommonBinding
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.project.adapter.ProjectChildAdapter
import com.hjc.wan.ui.project.contract.ProjectChildContract
import com.hjc.wan.ui.project.presenter.ProjectChildPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 项目子页面
 */
class ProjectChildFragment :
    BaseLazyFragment<FragmentCommonBinding, ProjectChildContract.View, ProjectChildPresenter>(),
    ProjectChildContract.View {

    private lateinit var mAdapter: ProjectChildAdapter

    private var cid: Int = 0

    private var mPage: Int = 1


    companion object {

        fun newInstance(cid: Int): ProjectChildFragment {
            val projectChildFragment = ProjectChildFragment()
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

    override fun initView() {
        super.initView()

        initLoadSir(mBinding.refreshLayout)

        val manager = LinearLayoutManager(mContext)
        mBinding.rvList.layoutManager = manager

        mAdapter = ProjectChildAdapter(null)
        mBinding.rvList.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initData() {
        EventManager.register(this)
        cid = arguments?.getInt("cid") ?: 0
        getPresenter()?.loadListData(mPage, cid, true)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 1) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getPresenter()?.loadListData(mPage, cid, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage, cid, false)
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

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        mPage = 1
        getPresenter()?.loadListData(mPage, cid, true)
    }

    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mAdapter.notifyDataSetChanged()
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishRefresh()
        mBinding.refreshLayout.finishLoadMore()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.CHANGE_LIST_ANIMATION) {
            SettingManager.getListAnimationType().let {
                if (it != 0) {
                    mAdapter.openLoadAnimation(it)
                } else {
                    mAdapter.closeLoadAnimation()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }
}
