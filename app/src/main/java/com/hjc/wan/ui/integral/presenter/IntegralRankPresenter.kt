package com.hjc.wan.ui.integral.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.integral.IntegralRankActivity
import com.hjc.wan.ui.integral.contract.IntegralRankContract

class IntegralRankPresenter : BasePresenter<IntegralRankContract.View>(), IntegralRankContract.Presenter {

    override fun loadListData(page: Int) {
        val activity = getView() as IntegralRankActivity

        RetrofitClient.getApi()
            .getIntegralRank(page)
            .compose(RxHelper.bind(activity))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<IntegralBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<IntegralBean>>?) {
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