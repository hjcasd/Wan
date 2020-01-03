package com.hjc.wan.ui.main.contract

import com.hjc.baselib.base.IBaseView

interface MainContract {

    interface View : IBaseView

    interface Presenter {
        fun requestPermission()
    }
}