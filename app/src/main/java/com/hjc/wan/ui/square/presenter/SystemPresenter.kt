package com.hjc.wan.ui.square.presenter

import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.SystemBean
import com.hjc.wan.ui.square.child.SystemFragment
import com.hjc.wan.ui.square.contract.SystemContract

class SystemPresenter : BasePresenter<SystemContract.View>(), SystemContract.Presenter{

    override fun loadListData() {
        val fragment = getView() as SystemFragment

        RetrofitClient.getApi()
            .getSystem()
            .compose(RxHelper.bind(fragment))
            .subscribe(object : CommonObserver<MutableList<SystemBean>>() {

                override fun onSuccess(result: MutableList<SystemBean>?) {
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