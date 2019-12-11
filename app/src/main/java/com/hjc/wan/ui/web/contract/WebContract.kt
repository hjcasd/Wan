package com.hjc.wan.ui.web.contract

import com.hjc.wan.base.IBaseView

interface WebContract {
    interface View : IBaseView {

    }

    interface Presenter {
        fun collectLink(name: String, link: String)
    }
}