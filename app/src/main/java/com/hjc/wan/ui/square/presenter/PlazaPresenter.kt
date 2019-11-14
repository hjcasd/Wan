package com.hjc.wan.ui.square.presenter

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.ui.square.contract.PlazaContract
import com.hjc.wan.ui.square.child.PlazaFragment
import com.hjc.wan.model.ArticleBean

class PlazaPresenter :BasePresenter<PlazaContract.View>(), PlazaContract.Presenter{

    override fun loadListData(page: Int) {
        val plazaFragment = getView() as PlazaFragment
        RetrofitClient.getApi()
            .getSquareData(page)
            .compose(RxHelper.bind(plazaFragment))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<ArticleBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<ArticleBean>>?) {
                    val data = result?.datas
                    if (data != null && data.size > 0) {
                        getView().showContent()
                        getView().showList(data)
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