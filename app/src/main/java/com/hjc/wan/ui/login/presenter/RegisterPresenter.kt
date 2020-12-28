package com.hjc.wan.ui.login.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.login.contract.RegisterContract

class RegisterPresenter : KotlinPresenter<RegisterContract.View>(), RegisterContract.Presenter {

    override fun register(username: String, password: String) {
        launchWrapper({
            RetrofitClient.getApi().register(username, password, password)
        }, {
            ToastUtils.showShort("注册成功")
            getView()?.toLogin()
        }, true)
    }

}
