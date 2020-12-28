package com.hjc.wan.ui.web.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.web.contract.WebContract

class WebPresenter : KotlinPresenter<WebContract.View>(), WebContract.Presenter {

    override fun collectLink(name: String, link: String) {
        launchWrapper({
            RetrofitClient.getApi().collectLink(name, link)
        }, {
            ToastUtils.showShort("收藏成功")
        }, true)
    }
}