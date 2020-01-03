package com.hjc.wan.ui.collect.contract

import com.hjc.baselib.base.IBaseView

interface CollectContract {

    interface View : IBaseView {
        fun showFragment()
    }

    interface Presenter {
        fun loadChild()
    }
}