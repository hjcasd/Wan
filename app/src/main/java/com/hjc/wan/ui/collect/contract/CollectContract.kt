package com.hjc.wan.ui.collect.contract

import com.hjc.wan.base.IBaseView

interface CollectContract {

    interface View : IBaseView {
        fun showFragment()
    }

    interface Presenter {
        fun loadChild()
    }
}