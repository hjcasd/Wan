package com.hjc.wan.ui.integral.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.IntegralHistoryBean
import com.hjc.wan.ui.integral.child.IntegralHistoryActivity
import com.hjc.wan.ui.integral.contract.IntegralHistoryContract

class IntegralHistoryPresenter : BasePresenter<IntegralHistoryContract.View>(), IntegralHistoryContract.Presenter {

    override fun loadListData(page: Int) {
        val activity = getView() as IntegralHistoryActivity

        RetrofitClient.getApi()
            .getIntegralHistory(page)
            .compose(RxHelper.bind(activity))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<IntegralHistoryBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<IntegralHistoryBean>>?) {
                    val data = result?.datas
                    data?.let {
                        if (data.size > 0) {
                            getView()?.showContent()
                            getView()?.showList(data)
                        } else {
                            if (page == 1) {
                                getView()?.showEmpty()
                            } else {
                                getView()?.showContent()
                                ToastUtils.showShort("没有更多数据了")
                            }
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