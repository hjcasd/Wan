package com.hjc.wan.ui.collect.presenter

import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.collect.contract.CollectLinkContract

class CollectLinkPresenter : KotlinPresenter<CollectLinkContract.View>(),
    CollectLinkContract.Presenter {

    override fun loadListData(isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getCollectLink()
        }, { result ->
            getView()?.refreshComplete()

            result?.let {
                if (it.size > 0) {
                    getView()?.showContent()
                    getView()?.showList(it)
                } else {
                    getView()?.showEmpty()
                }
            }
        }, isShowStatus = isFirst)
    }

}