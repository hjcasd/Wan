package com.hjc.wan.common

import com.hjc.baselib.base.IBaseView

interface CommonContract {

    interface View : IBaseView

    interface Presenter{
        fun show()
    }
}