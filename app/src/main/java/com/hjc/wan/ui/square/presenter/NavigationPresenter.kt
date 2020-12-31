package com.hjc.wan.ui.square.presenter

import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.square.contract.NavigationContract

class NavigationPresenter : KotlinPresenter<NavigationContract.View>(),
    NavigationContract.Presenter {

    override fun loadListData() {
        launchWrapper({
            RetrofitClient.getApi().getNavigation()
        }, { result ->
            if (result != null && result.size > 0) {
                getView()?.showContent()
                getView()?.showList(result)
            } else {
                getView()?.showEmpty()
            }
        }, isShowStatus = true, error = { e ->
            getView()?.showError()
        })
    }

}