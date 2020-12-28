package com.hjc.wan.ui.setting.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.setting.contract.SettingContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingPresenter : KotlinPresenter<SettingContract.View>(), SettingContract.Presenter {

    override fun logout() {
        launchWrapper({
            RetrofitClient.getApi().logout()
        }, {
            ToastUtils.showShort("退出成功")
            getView()?.toLogin()
        }, true)
    }

    override fun checkVersion() {
        mainScope.launch {
            delay(500)
            ToastUtils.showShort("已经是最新版本了")
        }
    }

}