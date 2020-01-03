package com.hjc.wan.ui.publics.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ClassifyBean

interface PublicContract {

    interface View : IBaseView {
        fun showIndicator(classifyList: MutableList<ClassifyBean>)
    }

    interface Presenter {
        fun loadPublicTitles()
    }

}