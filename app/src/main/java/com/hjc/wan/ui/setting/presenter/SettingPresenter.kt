package com.hjc.wan.ui.setting.presenter

import android.annotation.SuppressLint
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.ui.setting.SettingActivity
import com.hjc.wan.ui.setting.contract.SettingContract
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SettingPresenter : BasePresenter<SettingContract.View>(), SettingContract.Presenter {

    override fun logout() {
        val activity = getView() as SettingActivity

        RetrofitClient.getApi()
            .logout()
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                   getView()?.toLogin()
                }

            })
    }

    @SuppressLint("CheckResult")
    override fun checkVersion() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                ToastUtils.showShort("已经是最新版本了")
            }
    }

}