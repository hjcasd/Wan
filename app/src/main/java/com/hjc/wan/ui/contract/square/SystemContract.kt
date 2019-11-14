package com.hjc.wan.ui.contract.square

import com.hjc.wan.base.IBaseView
import com.hjc.wan.ui.model.SystemBean

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
