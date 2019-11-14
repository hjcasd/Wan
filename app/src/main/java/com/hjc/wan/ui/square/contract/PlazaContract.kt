package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.ArticleBean

interface PlazaContract {
    interface View : IBaseView {
        fun showContent()
        fun showEmpty()
        fun showError()
        fun showNoNetwork()

        fun showList(result: MutableList<ArticleBean>)
    }

    interface Presenter {
        fun loadListData(page: Int)
    }
}
