package com.hjc.wan.ui.collect.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.CollectArticleBean

interface CollectArticleContract {

    interface View : IBaseView {
        fun showList(result : MutableList<CollectArticleBean>)
        fun showUnCollectList(position : Int)
    }

    interface Presenter {
        fun loadListData(page: Int)
        fun unCollectArticle(bean : CollectArticleBean, position : Int)
    }
}