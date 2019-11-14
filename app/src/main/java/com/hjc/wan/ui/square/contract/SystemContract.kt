package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.SystemBean

interface SystemContract {

    interface View : IBaseView {
        fun showContent()
        fun showEmpty()
        fun showError()
        fun showNoNetwork()

        fun showList(result: MutableList<SystemBean>)
    }

    interface Presenter {
        fun loadListData()
    }
}
