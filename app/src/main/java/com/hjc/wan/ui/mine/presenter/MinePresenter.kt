package com.hjc.wan.ui.mine.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.mine.MineFragment
import com.hjc.wan.ui.mine.contract.MineContract

class MinePresenter : BasePresenter<MineContract.View>(), MineContract.Presenter {

    override fun getIntegralInfo(isShow: Boolean) {
        val mineFragment = getView() as MineFragment
        RetrofitClient.getApi()
            .getIntegral()
            .compose(RxHelper.bind(mineFragment))
            .subscribe(object : ProgressObserver<IntegralBean>(mineFragment.childFragmentManager, isShow) {

                override fun onSuccess(result: IntegralBean?) {
                    if (result != null){
                        getView().showIntegral(result)
                    }else{
                        ToastUtils.showShort("获取积分数据失败")
                    }
                }

            })
    }

}