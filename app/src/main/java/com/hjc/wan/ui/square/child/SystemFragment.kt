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

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.visibility = View.GONE
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }


    override fun initData() {
        super.initData()

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