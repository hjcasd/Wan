package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.NavigationBean

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