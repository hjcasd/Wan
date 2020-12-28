package com.hjc.wan.ui.collect.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.CollectArticleBean

interface CollectArticleContract {

    interface View : IBaseView {
        fun showList(result : MutableList<CollectArticleBean>)
        fun showUnCollectList(position : Int)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, isFirst: Boolean)
        fun unCollectArticle(bean : CollectArticleBean, position : Int)
    }
}