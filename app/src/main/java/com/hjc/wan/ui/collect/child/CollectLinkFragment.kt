package com.hjc.wan.ui.collect.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

        initLoadSir(smartRefreshLayout)
        smartRefreshLayout.setEnableLoadMore(false)

        val manager = LinearLayoutManager(mContext)
        rvCommon.layoutManager = manager

        mAdapter = CollectLinkAdapter(null)
        rvCommon.adapter = mAdapter

        smartRefreshLayout.setEnableLoadMore(false)

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initData() {
        super.initData()

        getPresenter()?.loadListData(true)
    }

    override fun showList(result: MutableList<CollectLinkBean>) {
        mAdapter.setNewData(result)
    }

    override fun refreshComplete() {
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        smartRefreshLayout.setOnRefreshListener {
            getPresenter()?.loadListData(false)
        }

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                RouterManager.jumpToWeb(bean.name, bean.link)
            }
        }
    }

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        getPresenter()?.loadListData(true)
    }
}