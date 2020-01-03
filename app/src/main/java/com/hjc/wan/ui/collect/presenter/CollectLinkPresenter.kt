package com.hjc.wan.ui.collect.presenter

import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.collect.child.CollectLinkFragment
import com.hjc.wan.ui.collect.contract.CollectLinkContract

class CollectLinkPresenter : BasePresenter<CollectLinkContract.View>(),
    CollectLinkContract.Presenter {

    override fun loadListData() {
        val fragment = getView() as CollectLinkFragment

        RetrofitClient.getApi()
            .getCollectLink()
            .compose(RxHelper.bind(fragment))
            .subscribe(object : CommonObserver<MutableList<CollectLinkBean>>() {

                override fun onSuccess(result: MutableList<CollectLinkBean>?) {
                    result?.let {
                        if (result.size > 0) {
                            getView()?.showContent()
                            getView()?.showList(result)
                        } else {
                            getView()?.showEmpty()
                        }
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