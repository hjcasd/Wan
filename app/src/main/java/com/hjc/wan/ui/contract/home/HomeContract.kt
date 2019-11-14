package com.hjc.wan.ui.contract.home

import com.hjc.wan.base.IBaseView
import com.hjc.wan.ui.model.ArticleBean
import com.hjc.wan.ui.model.BannerBean

interface HomeContract {
    interface View : IBaseView {
        fun showContent()
        fun showError()
        fun showEmpty()
        fun showNoNetwork()

        fun showBanner(result: MutableList<BannerBean>)
        fun showList(result: MutableList<ArticleBean>)
    }

    interface Presenter {
        fun loadBannerData()
        fun loadListData(page: Int)
    }
}
