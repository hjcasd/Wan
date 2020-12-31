package com.hjc.wan.ui.collect.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.model.CollectArticleBean
import com.hjc.wan.ui.collect.contract.CollectArticleContract

class CollectArticlePresenter : KotlinPresenter<CollectArticleContract.View>(),
    CollectArticleContract.Presenter {

    override fun loadListData(page: Int, isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi()
                .getCollectArticle(page)
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
        }, isShowStatus = isFirst, error = { e ->
            getView()?.refreshComplete()
            getView()?.showError()
        })
    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean: CollectArticleBean, position: Int) {
        launchWrapper({
            RetrofitClient.getApi().unCollectOrigin(bean.id, bean.originId)
        }, {
            ToastUtils.showShort("已取消收藏")
            getView()?.showUnCollectList(position)
        }, true)
    }

}