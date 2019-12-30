package com.hjc.wan.ui.setting.contract

import com.hjc.wan.base.IBaseView

interface SettingContract {

    interface View : IBaseView {
        fun changeListAnimation()
        fun changeTheme()
    }

    interface Presenter{
        fun logout()
        fun checkVersion()
    }
}