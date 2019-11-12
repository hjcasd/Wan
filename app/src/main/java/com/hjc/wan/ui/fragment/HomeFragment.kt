package com.hjc.wan.ui.fragment

import android.os.Bundle
import android.view.View
import com.hjc.baselib.fragment.BaseFragment
import com.hjc.wan.R

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:35
 * @Description: 首页
 */
class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View) {

    }

}
