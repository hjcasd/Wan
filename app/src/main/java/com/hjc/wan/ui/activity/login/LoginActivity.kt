package com.hjc.wan.ui.activity.login

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.contract.login.LoginContract
import com.hjc.wan.ui.presenter.login.LoginPresenter

/**
 * @Author: HJC
 * @Date: 2019/11/11 15:38
 * @Description: 登录页面
 */
@Route(path = RoutePath.URL_LOGIN)
class LoginActivity: BaseMvpActivity<LoginContract.View, LoginPresenter>(), LoginContract.View{

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun createView(): LoginContract.View {
        return this
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    override fun toMain() {

    }

    override fun toRegister() {

    }

}