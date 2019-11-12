package com.hjc.wan.ui.fragment

import android.os.Bundle
import android.view.View
import com.hjc.baselib.fragment.BaseFragment
import com.hjc.wan.R

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 广场页面
 */
class SquareFragment : BaseFragment() {

    companion object {

        fun newInstance(): SquareFragment {
            return SquareFragment()
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_square
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun addListeners() {
    }

    override fun onSingleClick(v: View?) {
    }


}
