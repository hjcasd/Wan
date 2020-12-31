package com.hjc.wan.ui.collect.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.fragment.BaseLazyFragment
import com.hjc.wan.databinding.FragmentCommonBinding
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.collect.adapter.CollectLinkAdapter
import com.hjc.wan.ui.collect.contract.CollectLinkContract
import com.hjc.wan.ui.collect.presenter.CollectLinkPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:40
 * @Description: 收藏网址页面
 */
class CollectLinkFragment :
    BaseLazyFragment<FragmentCommonBinding, CollectLinkContract.View, CollectLinkPresenter>(),
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

    override fun initView() {
        super.initView()

        initLoadSir(mBinding.refreshLayout)
        mBinding.refreshLayout.setEnableLoadMore(false)

        val manager = LinearLayoutManager(mContext)
        mBinding.rvList.layoutManager = manager

        mAdapter = CollectLinkAdapter(null)
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
        getPresenter()?.loadListData(true)
    }

    override fun showList(result: MutableList<CollectLinkBean>) {
        mAdapter.setNewData(result)
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishRefresh()
        mBinding.refreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        mBinding.refreshLayout.setOnRefreshListener {
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