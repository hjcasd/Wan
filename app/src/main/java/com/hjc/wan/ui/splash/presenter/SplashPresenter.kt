package com.hjc.wan.ui.splash.presenter

import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.ui.splash.contract.SplashContract
import com.hjc.wan.utils.helper.AccountManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashPresenter : KotlinPresenter<SplashContract.View>(), SplashContract.Presenter {

    override fun startCountdown() {
        mainScope.launch {
            delay(500)
            if (AccountManager.isLogin()) {
                getView()?.toMain()
            } else {
                getView()?.toLogin()
            }
        }
    }

}