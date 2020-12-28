package com.hjc.wan.ui.login.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.login.contract.LoginContract
import com.hjc.wan.utils.helper.AccountManager

class LoginPresenter : KotlinPresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun login(username: String, password: String) {
        launchWrapper({
            RetrofitClient.getApi().login(username, password)
        }, { result ->
            if (result != null) {
                ToastUtils.showShort("登录成功")
                AccountManager.setLogin(true)
                AccountManager.setLoginInfo(result)
                getView()?.toMain()
            } else {
                ToastUtils.showShort("登录失败,请稍后重试")
            }
        }, true)
    }

}
