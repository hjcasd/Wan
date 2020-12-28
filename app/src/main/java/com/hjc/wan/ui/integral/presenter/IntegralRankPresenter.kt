package com.hjc.wan.ui.integral.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.integral.contract.IntegralRankContract

class IntegralRankPresenter : KotlinPresenter<IntegralRankContract.View>(), IntegralRankContract.Presenter {

    override fun loadListData(page: Int, isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getIntegralRank(page)
        }, { result ->
            getView()?.refreshComplete()

            val data = result?.datas
            data?.let {
                if (it.size > 0) {
                    getView()?.showContent()
                    getView()?.showList(it)
                } else {
                    if (page == 1) {
                        getView()?.showEmpty()
                    } else {
                        getView()?.showContent()
                        ToastUtils.showShort("没有更多数据了")
                    }
                }
            }
        }, isShowStatus = isFirst)
    }

}