package com.hjc.wan.ui.square.presenter

import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.NavigationBean
import com.hjc.wan.ui.square.child.NavigationFragment
import com.hjc.wan.ui.square.contract.NavigationContract

class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {

    override fun loadListData() {
        val fragment = getView() as NavigationFragment

        RetrofitClient.getApi()
            .getNavigation()
            .compose(RxHelper.bind(fragment))
            .subscribe(object : CommonObserver<MutableList<NavigationBean>>() {

                override fun onSuccess(result: MutableList<NavigationBean>?) {
                    if (result != null && result.size > 0) {
                        getView()?.showContent()
                        getView()?.showList(result)
                    } else {
                        getView()?.showEmpty()
                    }
                }

                override fun onFailure(errorMsg: String) {
                    super.onFailure(errorMsg)
                    if (errorMsg == "网络不可用" || errorMsg == "请求网络超时") {
                        getView()?.showNoNetwork()
                    } else {
                        getView()?.showError()
                    }
                }

            })
    }

}