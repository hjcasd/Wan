package com.hjc.wan.ui.login.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.ui.login.contract.LoginContract
import com.hjc.wan.model.LoginBean
import com.hjc.wan.ui.login.LoginActivity
import com.hjc.wan.utils.helper.AccountManager

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun login(username: String, password: String) {
        val loginActivity = getView() as LoginActivity

        RetrofitClient.getApi()
            .login(username, password)
            .compose(RxHelper.bind(loginActivity))
            .subscribe(object : ProgressObserver<LoginBean>(loginActivity.supportFragmentManager) {

                override fun onSuccess(result: LoginBean?) {
                    if (result != null) {
                        ToastUtils.showShort("登录成功")
                        AccountManager.setLogin(true)
                        AccountManager.setLoginInfo(result)
                        getView().toMain()
                    } else {
                        ToastUtils.showShort("登录失败,请稍后重试")
                    }
                }
            })
    }

}
