package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.ArticleBean

interface PlazaContract {

    interface View : IBaseView {
        fun showList(result: MutableList<ArticleBean>)
        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)
    }

    interface Presenter {
        fun loadListData(page: Int)
        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}
