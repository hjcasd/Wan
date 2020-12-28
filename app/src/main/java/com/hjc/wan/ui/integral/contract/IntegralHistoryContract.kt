package com.hjc.wan.ui.integral.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.IntegralHistoryBean

interface IntegralHistoryContract {

    interface View : IBaseView {
        fun showList(result: MutableList<IntegralHistoryBean>)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, isFirst: Boolean)
    }
}