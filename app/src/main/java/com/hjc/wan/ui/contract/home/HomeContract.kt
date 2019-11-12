package com.hjc.wan.ui.contract.home

import com.hjc.wan.base.IBaseView
import com.hjc.wan.ui.model.BannerBean
import com.hjc.wan.ui.model.HomeArticleBean

interface HomeContract {
    interface View : IBaseView {
        fun showContent()
        fun showError()
        fun showEmpty()
        fun showNoNetwork()

        fun showBanner(result: MutableList<BannerBean>)
        fun showList(result: MutableList<HomeArticleBean>)
    }

    interface Presenter {
        fun loadBannerData()
        fun loadArticleData(page: Int, isFirst: Boolean)
    }
}
