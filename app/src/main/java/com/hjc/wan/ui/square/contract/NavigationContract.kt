package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.NavigationBean

interface NavigationContract {

    interface View : IBaseView {
        fun showList(result: MutableList<NavigationBean>)
    }

    interface Presenter {
        fun loadListData()
    }
}