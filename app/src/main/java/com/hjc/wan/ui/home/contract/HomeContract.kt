package com.hjc.wan.ui.home.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.BannerBean

interface HomeContract {
    interface View : IBaseView {
        fun showContent()
        fun showError()
        fun showEmpty()
        fun showNoNetwork()

        fun showBanner(result: MutableList<BannerBean>)
        fun showList(result: MutableList<ArticleBean>)

        fun showCollectList(bean : ArticleBean)
        fun showUnCollectList(bean : ArticleBean)
    }

    interface Presenter {
        fun loadBannerData()
        fun loadListData(page: Int)
        fun collectArticle(bean : ArticleBean)
        fun unCollectArticle(bean : ArticleBean)
    }
}
