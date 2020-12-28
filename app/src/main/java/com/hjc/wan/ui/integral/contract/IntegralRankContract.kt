package com.hjc.wan.ui.integral.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.IntegralBean

interface IntegralRankContract {

    interface View : IBaseView {
        fun showList(result: MutableList<IntegralBean>)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, isFirst: Boolean)
    }
}