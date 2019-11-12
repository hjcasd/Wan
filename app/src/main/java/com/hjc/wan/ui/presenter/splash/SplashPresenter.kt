package com.hjc.wan.ui.presenter.splash

import android.annotation.SuppressLint
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.ui.contract.splash.SplashContract
import com.hjc.wan.utils.helper.AccountManager
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashPresenter : BasePresenter<SplashContract.View>(),
    SplashContract.Presenter {

    @SuppressLint("CheckResult")
    override fun startCountdown() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                if (AccountManager.isLogin()) {
                   getView().toMain()
                } else {
                    getView().toLogin()
                }
            }
    }

}