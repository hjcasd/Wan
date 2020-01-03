package com.hjc.wan.ui.square.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseMvpLazyFragment
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.square.adapter.PlazaAdapter
import com.hjc.wan.ui.square.contract.PlazaContract
import com.hjc.wan.ui.square.presenter.PlazaPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_common.*

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 广场子页面
 */
class PlazaFragment : BaseMvpLazyFragment<PlazaContract.View, PlazaPresenter>(),
    PlazaContract.View {

    private lateinit var mAdapter: PlazaAdapter

    private var mPage = 0


    companion object {

        fun newInstance(): PlazaFragment {
            return PlazaFragment()
        }
    }

    override fun createPresenter(): PlazaPresenter {
        return PlazaPresenter()
    }

    override fun createView(): PlazaContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_common
    }

    override fun initView() {
        super.initView()

        val manager =LinearLayoutManager(mContext)
        rvCommon.layoutManager = manager

        mAdapter = PlazaAdapter(null)
        rvCommon.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.visibility= View.GONE
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter()?.loadListData(mPage)
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter()?.loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
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

    override fun handleMessage(event: MessageEvent<*>?) {
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

}