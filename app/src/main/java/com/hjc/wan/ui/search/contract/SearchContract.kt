package com.hjc.wan.ui.search.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.SearchBean
import com.hjc.wan.model.db.History

interface SearchContract {

    interface View : IBaseView {
        fun showHotTag(result: MutableList<SearchBean>)
        fun showList(result: MutableList<ArticleBean>)
        fun showHistory(result: List<History>)
        fun hideHistory()

        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)
        fun refreshComplete()
    }

    interface Presenter {
        fun getHotKey()
        fun getHistory()
        fun search(page: Int, key: String, isShow: Boolean)

        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}