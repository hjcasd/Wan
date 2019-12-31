package com.hjc.wan.ui.collect.child

import android.annotation.SuppressLint
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.collect.adapter.CollectLinkAdapter
import com.hjc.wan.ui.collect.contract.CollectLinkContract
import com.hjc.wan.ui.collect.presenter.CollectLinkPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_collect_link.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

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
        return R.layout.fragment_collect_link
    }

    override fun initView() {
        super.initView()
        val manager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        rvCollectLink.layoutManager = manager

        mAdapter = CollectLinkAdapter(null)
        rvCollectLink.adapter = mAdapter

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
        }
    }

    override fun initData() {
        super.initData()
        EventManager.register(this)

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
                val bean =  mAdapter.data[position]

                RouterManager.jumpToWeb(bean.name, bean.link)
            }
        }
    }


    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                stateView.showContent()
                smartRefreshLayout.finishRefresh()
            }
    }

    override fun showLoading() {
        stateView.showLoading()
    }

    override fun showError() {
        stateView.showError()
        smartRefreshLayout.finishRefresh()
    }

    override fun showEmpty() {
        stateView.showEmpty()
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoNetwork() {
        stateView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<Any>) {
        if (event.code == EventCode.CHANGE_LIST_ANIMATION) {
            if (SettingManager.getListAnimationType() != 0) {
                mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

}