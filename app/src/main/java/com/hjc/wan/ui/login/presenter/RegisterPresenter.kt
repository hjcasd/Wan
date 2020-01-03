package com.hjc.wan.ui.login.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.ui.login.RegisterActivity
import com.hjc.wan.ui.login.contract.RegisterContract

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    override fun register(username: String, password: String) {
        val activity = getView() as RegisterActivity

        RetrofitClient.getApi()
            .register(username, password, password)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("注册成功")
                    getView()?.toLogin()
                }
            })
    }

}
