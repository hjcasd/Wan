package com.hjc.wan.ui.square.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseLazyFragment
import com.hjc.wan.constant.EventCode
import com.hjc.wan.databinding.FragmentCommonBinding
import com.hjc.wan.model.SystemBean
import com.hjc.wan.ui.square.adapter.SystemAdapter
import com.hjc.wan.ui.square.contract.SystemContract
import com.hjc.wan.ui.square.presenter.SystemPresenter
import com.hjc.wan.utils.helper.SettingManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 体系子页面
 */
class SystemFragment : BaseLazyFragment<FragmentCommonBinding, SystemContract.View, SystemPresenter>(),
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

    override fun initView() {
        super.initView()

        initLoadSir(mBinding.refreshLayout)
        mBinding.refreshLayout.setEnableLoadMore(false)

        val manager = LinearLayoutManager(mContext)
        mBinding.rvList.layoutManager = manager

        mAdapter = SystemAdapter(null)
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
        getPresenter()?.loadListData(true)
    }

    override fun showList(result: MutableList<SystemBean>) {
        mAdapter.setNewData(result)
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishRefresh()
    }

    override fun addListeners() {
        mBinding.refreshLayout.setOnRefreshListener { getPresenter()?.loadListData(false) }
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

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }

}