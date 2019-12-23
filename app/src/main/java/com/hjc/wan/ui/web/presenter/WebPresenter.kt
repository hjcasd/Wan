package com.hjc.wan.ui.web.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.CollectLinkBean
import com.hjc.wan.ui.web.WebActivity
import com.hjc.wan.ui.web.contract.WebContract

class WebPresenter : BasePresenter<WebContract.View>(), WebContract.Presenter {

    override fun collectLink(name: String, link: String) {
        val activity = getView() as WebActivity

        RetrofitClient.getApi()
            .collectLink(name, link)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<CollectLinkBean>(activity.supportFragmentManager){

                override fun onSuccess(result: CollectLinkBean?) {
                    ToastUtils.showShort("收藏成功")
                }

            })
    }
}