package com.hjc.wan.ui.home.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.BannerBean

interface HomeContract {

    interface View : IBaseView {
        fun showBanner(result: MutableList<BannerBean>)
        fun showList(result: MutableList<ArticleBean>)

        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)

        fun refreshComplete()
    }

    interface Presenter {
        fun loadBannerData()
        fun loadListData(page: Int, isFirst: Boolean)
        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}
