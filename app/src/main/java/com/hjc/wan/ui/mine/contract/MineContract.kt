package com.hjc.wan.ui.mine.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.IntegralBean

interface MineContract {

    interface View : IBaseView {
        fun showIntegral(result : IntegralBean)
    }

    interface Presenter {
        fun loadIntegralData(isShow: Boolean)
    }
}
