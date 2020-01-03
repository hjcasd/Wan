package com.hjc.wan.ui.setting.contract

import com.hjc.baselib.base.IBaseView

interface SettingContract {

    interface View : IBaseView {
        fun toLogin()
    }

    interface Presenter{
        fun logout()
        fun checkVersion()
    }
}