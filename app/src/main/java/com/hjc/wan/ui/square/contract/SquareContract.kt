package com.hjc.wan.ui.square.contract

import com.hjc.wan.base.IBaseView

interface SquareContract {
    interface View : IBaseView {
        fun showIndicator(titleList: MutableList<String>)
    }

    interface Presenter {
        fun getSquareTitles()
    }
}
