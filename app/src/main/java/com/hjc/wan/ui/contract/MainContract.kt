package com.hjc.wan.ui.contract

import com.hjc.wan.base.IBaseView

interface MainContract {

    interface View : IBaseView {

    }

    interface Presenter {
        fun requestPermission()
    }
}