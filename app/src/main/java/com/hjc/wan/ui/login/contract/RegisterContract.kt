package com.hjc.wan.ui.login.contract

import com.hjc.wan.base.IBaseView

interface RegisterContract {

    interface View : IBaseView {
        fun toLogin()
    }

    interface Presenter {
        fun register(username: String, password: String)
    }
}
