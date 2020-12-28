package com.hjc.wan.ui.square.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ArticleBean

interface PlazaContract {

    interface View : IBaseView {
        fun showList(result: MutableList<ArticleBean>)
        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, isFirst: Boolean)
        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}
