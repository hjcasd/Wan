package com.hjc.wan.ui.home.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.home.contract.HomeContract

class HomePresenter : KotlinPresenter<HomeContract.View>(), HomeContract.Presenter {

    /**
     * 加载banner
     */
    override fun loadBannerData() {
        launchWrapper({
            RetrofitClient.getApi().getBanner()
        }, { result ->
            if (result != null && result.size > 0) {
                getView()?.showBanner(result)
            } else {
                ToastUtils.showShort("Banner数据请求失败")
            }
        })
    }

    /**
     * 加载列表
     */
    override fun loadListData(page: Int, isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getArticle(page)
        }, { result ->
            getView()?.refreshComplete()

            val data = result?.datas
            data?.let {
                if (it.size > 0) {
                    getView()?.showContent()
                    getView()?.showList(it)
                } else {
                    if (page == 0) {
                        getView()?.showEmpty()
                    } else {
                        getView()?.showContent()
                        ToastUtils.showShort("没有更多数据了")
                    }
                }
            }
        }, isShowStatus = isFirst)
    }

    /**
     * 收藏
     */
    override fun collectArticle(bean: ArticleBean) {
        launchWrapper({
            RetrofitClient.getApi().collect(bean.id)
        }, {
            ToastUtils.showShort("收藏成功")
            getView()?.showCollectList(bean)
        }, true)
    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean: ArticleBean) {
        launchWrapper({
            RetrofitClient.getApi().unCollect(bean.id)
        }, {
            ToastUtils.showShort("已取消收藏")
            getView()?.showUnCollectList(bean)
        }, true)
    }

}