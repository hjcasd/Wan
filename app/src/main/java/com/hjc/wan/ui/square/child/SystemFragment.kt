package com.hjc.wan.ui.square.child

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.SystemBean
import com.hjc.wan.ui.square.adapter.SystemAdapter
import com.hjc.wan.ui.square.contract.SystemContract
import com.hjc.wan.ui.square.presenter.SystemPresenter
import com.hjc.wan.utils.helper.SettingManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_system.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 体系子页面
 */
class SystemFragment : BaseMvpLazyFragment<SystemContract.View, SystemPresenter>(),
    SystemContract.View {

    private lateinit var mAdapter: SystemAdapter

    companion object {

        fun newInstance(): SystemFragment {
            return SystemFragment()
        }
    }

    override fun createPresenter(): SystemPresenter {
        return SystemPresenter()
    }

    override fun createView(): SystemContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvSystem.layoutManager = manager

        mAdapter = SystemAdapter(null)
        rvSystem.adapter = mAdapter

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

    override fun showList(result: MutableList<SystemBean>) {
        mAdapter.setNewData(result)
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshListener { getPresenter()?.loadListData() }
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