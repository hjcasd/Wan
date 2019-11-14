package com.hjc.wan.ui.contract.square

import com.hjc.wan.base.IBaseView
import com.hjc.wan.ui.model.NavigationBean

interface NavigationContract {

    interface View : IBaseView {
        fun showContent()
        fun showEmpty()
        fun showError()
        fun showNoNetwork()

        fun showList(result: MutableList<NavigationBean>)
    }

    interface Presenter {
        fun loadListData()
    }
}