package com.hjc.wan.ui.presenter.login

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.ui.contract.login.LoginContract

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun login(phone: String, password: String) {
    }

}
