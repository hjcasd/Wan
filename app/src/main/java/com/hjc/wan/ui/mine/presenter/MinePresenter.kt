package com.hjc.wan.ui.mine.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.mine.contract.MineContract

class MinePresenter : KotlinPresenter<MineContract.View>(), MineContract.Presenter {

    override fun loadIntegralData(isShow: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getIntegral()
        }, { result ->
            if (result != null) {
                getView()?.showIntegral(result)
            } else {
                ToastUtils.showShort("获取积分数据失败")
            }
        }, isShow)
    }

}