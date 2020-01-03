package com.hjc.wan.ui.collect.child

import android.view.View
import com.hjc.baselib.fragment.BaseMvpLazyFragment
import com.hjc.wan.R
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.collect.adapter.CollectLinkAdapter
import com.hjc.wan.ui.collect.contract.CollectLinkContract
import com.hjc.wan.ui.collect.presenter.CollectLinkPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import kotlinx.android.synthetic.main.fragment_common.*

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:40
 * @Description: 收藏网址页面
 */
class CollectLinkFragment :
    BaseMvpLazyFragment<CollectLinkContract.View, CollectLinkPresenter>(),
    CollectLinkContract.View {

    private lateinit var mAdapter: CollectLinkAdapter


    companion object {

        fun newInstance(): CollectLinkFragment {
            return CollectLinkFragment()
        }
    }

    override fun createPresenter(): CollectLinkPresenter {
        return CollectLinkPresenter()
    }

    override fun createView(): CollectLinkContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_common
    }

    override fun initView() {
        super.initView()
        val manager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        rvCommon.layoutManager = manager

        mAdapter = CollectLinkAdapter(null)
        rvCommon.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()
        smartRefreshLayout.setEnableRefresh(true)
    }

    override fun initTitleBar() {
        super.initTitleBar()
        titleBar.visibility = View.GONE
    }


    override fun initData() {
        super.initData()

        showLoading()
        getPresenter()?.loadListData()
    }

    override fun showList(result: MutableList<CollectLinkBean>) {
        mAdapter.setNewData(result)
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshListener {
            getPresenter()?.loadListData()
        }

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]

                RouterManager.jumpToWeb(bean.name, bean.link)
            }
        }
    }
}