package com.hjc.wan.ui.presenter.square

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.ui.contract.square.SystemContract
import com.hjc.wan.ui.fragment.square.child.SystemFragment
import com.hjc.wan.ui.model.SystemBean

class SystemPresenter :BasePresenter<SystemContract.View>(), SystemContract.Presenter{

    override fun loadListData() {
        val systemFragment = getView() as SystemFragment
        RetrofitClient.getApi()
            .getSystemData()
            .compose(RxHelper.bind(systemFragment))
            .subscribe(object : CommonObserver<MutableList<SystemBean>>() {

                override fun onSuccess(result: MutableList<SystemBean>?) {
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