package com.hjc.wan.ui.square.contract

import com.hjc.baselib.base.IBaseView

interface SquareContract {

    interface View : IBaseView {
        fun showIndicator(titleList: MutableList<String>)
    }

    interface Presenter {
        fun loadSquareTitles()
    }
}
