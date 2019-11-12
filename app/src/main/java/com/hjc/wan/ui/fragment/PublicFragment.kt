package com.hjc.wan.ui.fragment

import android.os.Bundle
import android.view.View
import com.hjc.baselib.fragment.BaseFragment
import com.hjc.wan.R

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 公众号页面
 */
class PublicFragment : BaseFragment() {

    companion object {

        fun newInstance(): PublicFragment {
            return PublicFragment()
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_public
    }

    override fun initView() {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun addListeners() {
    }

    override fun onSingleClick(v: View?) {
    }


}
