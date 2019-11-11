package com.hjc.wan.ui.contract.splash

import com.hjc.wan.base.IBaseView


interface SplashContract {

    interface View : IBaseView {
        fun toLogin()
    }

    interface Presenter{
        fun startCountdown()
    }
}