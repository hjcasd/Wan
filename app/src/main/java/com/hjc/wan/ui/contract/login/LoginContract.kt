package com.hjc.wan.ui.contract.login

import com.hjc.wan.base.IBaseView

interface LoginContract {

    interface View : IBaseView {
        fun toMain()
        fun toRegister()
    }

    interface Presenter {
        fun login(username: String, password: String)
    }
}
