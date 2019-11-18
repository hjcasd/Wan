package com.hjc.wan.ui.mine.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.IntegralBean

interface MineContract {
    interface View : IBaseView {
        fun showIntegral(result : IntegralBean)
        fun toIntegral()
        fun toCollect()
        fun toArticle()
        fun toTodo()
    }

    interface Presenter {
        fun getIntegralInfo(isShow: Boolean)
    }
}
