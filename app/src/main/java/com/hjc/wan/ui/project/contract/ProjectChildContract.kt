package com.hjc.wan.ui.project.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ArticleBean

interface ProjectChildContract {

    interface View : IBaseView {
        fun showList(result: MutableList<ArticleBean>)
        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, cid: Int, isFirst: Boolean)
        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}
