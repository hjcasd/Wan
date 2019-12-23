package com.hjc.wan.ui.integral.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.IntegralHistoryBean

interface IntegralHistoryContract {

    interface View : IBaseView {
        fun showList(result : MutableList<IntegralHistoryBean>)
    }

    interface Presenter {
        fun loadListData(page: Int)
    }
}