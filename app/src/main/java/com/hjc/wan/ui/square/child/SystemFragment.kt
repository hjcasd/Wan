package com.hjc.wan.ui.square.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseMvpLazyFragment
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.model.SystemBean
import com.hjc.wan.ui.square.adapter.SystemAdapter
import com.hjc.wan.ui.square.contract.SystemContract
import com.hjc.wan.ui.square.presenter.SystemPresenter
import com.hjc.wan.utils.helper.SettingManager
import kotlinx.android.synthetic.main.fragment_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        return R.layout.fragment_common
    }

    override fun initView() {
        super.initView()

        initLoadSir(smartRefreshLayout)
        smartRefreshLayout.setEnableLoadMore(false)

        val manager = LinearLayoutManager(mContext)
        rvCommon.layoutManager = manager

        mAdapter = SystemAdapter(null)
        rvCommon.adapter = mAdapter

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

    override fun showList(result: MutableList<SystemBean>) {
        mAdapter.setNewData(result)
    }

    override fun refreshComplete() {
        smartRefreshLayout.finishRefresh()
    }

    override fun addListeners() {
        smartRefreshLayout.setOnRefreshListener { getPresenter()?.loadListData(false) }
    }

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        getPresenter()?.loadListData(true)
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

}