package com.hjc.wan.ui.collect.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.CollectLinkBean

interface CollectLinkContract {

    interface View : IBaseView {
        fun showList(result : MutableList<CollectLinkBean>)
    }

    interface Presenter {
        fun loadListData()
    }
}