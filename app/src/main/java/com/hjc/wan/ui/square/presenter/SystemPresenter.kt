package com.hjc.wan.ui.square.presenter

import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.square.contract.SystemContract

class SystemPresenter : KotlinPresenter<SystemContract.View>(), SystemContract.Presenter {

    override fun loadListData(isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getSystem()
        }, { result ->
            getView()?.refreshComplete()

            if (result != null && result.size > 0) {
                getView()?.showContent()
                getView()?.showList(result)
            } else {
                getView()?.showEmpty()
            }
        }, isShowStatus = isFirst, error = { e ->
            getView()?.refreshComplete()
            getView()?.showError()
        })
    }

}