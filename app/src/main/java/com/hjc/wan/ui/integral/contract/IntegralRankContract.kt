package com.hjc.wan.ui.integral.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.IntegralBean

interface IntegralRankContract {

    interface View : IBaseView {
        fun showList(result : MutableList<IntegralBean>)
    }

    interface Presenter {
        fun loadListData(page: Int)
    }
}