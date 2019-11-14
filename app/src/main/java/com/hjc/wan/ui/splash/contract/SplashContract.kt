package com.hjc.wan.ui.splash.contract

import com.hjc.wan.base.IBaseView


interface SplashContract {

    interface View : IBaseView {
        fun toLogin()
        fun toMain()
    }

    interface Presenter{
        fun startCountdown()
    }
}