package com.hjc.wan.ui.setting.contract

import com.hjc.wan.base.IBaseView

interface SettingContract {

    interface View : IBaseView {
    }

    interface Presenter{
        fun logout()
        fun checkVersion()
    }
}