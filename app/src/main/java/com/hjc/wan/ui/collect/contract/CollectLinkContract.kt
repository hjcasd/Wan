package com.hjc.wan.ui.collect.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.CollectLinkBean

interface CollectLinkContract {

    interface View : IBaseView {
        fun showList(result : MutableList<CollectLinkBean>)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(isFirst: Boolean)
    }
}