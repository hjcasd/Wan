package com.hjc.wan.ui.publics.child

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.databinding.FragmentCommonBinding
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.publics.adapter.PublicChildAdapter
import com.hjc.wan.ui.publics.contract.PublicChildContract
import com.hjc.wan.ui.publics.presenter.PublicChildPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author: HJC
 * @Date: 2019/11/18 10:54
 * @Description: 公众号子页面
 */
class PublicChildFragment : BaseLazyFragment<FragmentCommonBinding, PublicChildContract.View, PublicChildPresenter>(),
    PublicChildContract.View {

    private lateinit var mAdapter: PublicChildAdapter

    private var cid: Int = 0

    private var mPage: Int = 1


    companion object {

        fun newInstance(cid: Int): PublicChildFragment {
            val publicChildFragment= PublicChildFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            publicChildFragment.arguments = bundle
            return publicChildFragment
        }
    }

    override fun createPresenter(): PublicChildPresenter {
        return PublicChildPresenter()
    }

    override fun createView(): PublicChildContract.View {
        return this
    }

    override fun initView() {
        super.initView()

        initLoadSir( mBinding.refreshLayout)

        val manager = LinearLayoutManager(mContext)
        mBinding.rvList.layoutManager = manager

        mAdapter = PublicChildAdapter(null)
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
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

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