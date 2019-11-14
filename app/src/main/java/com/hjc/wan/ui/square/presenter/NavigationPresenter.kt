package com.hjc.wan.ui.square.presenter

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.ui.square.contract.NavigationContract
import com.hjc.wan.ui.square.child.NavigationFragment
import com.hjc.wan.model.NavigationBean

class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {

    override fun loadListData() {
        val navigationFragment = getView() as NavigationFragment
        RetrofitClient.getApi()
            .getNavigationData()
            .compose(RxHelper.bind(navigationFragment))
            .subscribe(object : CommonObserver<MutableList<NavigationBean>>() {

                override fun onSuccess(result: MutableList<NavigationBean>?) {
                    if (result != null && result.size > 0) {
                        getView().showContent()
                        getView().showList(result)
                    } else {
                        getView().showEmpty()
                    }
                }

                override fun onFailure(errorMsg: String) {
                    super.onFailure(errorMsg)
                    if (errorMsg == "网络不可用" || errorMsg == "请求网络超时") {
                        getView().showNoNetwork()
                    } else {
                        getView().showError()
                    }
                }

            })
    }

}