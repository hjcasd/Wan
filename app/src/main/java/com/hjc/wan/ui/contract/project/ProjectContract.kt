package com.hjc.wan.ui.contract.project

import com.hjc.wan.base.IBaseView
import com.hjc.wan.ui.model.ClassifyBean

interface ProjectContract {
    interface View : IBaseView {
        fun showIndicator(classifyList: MutableList<ClassifyBean>)
    }

    interface Presenter {
        fun getProjectTitles()
    }
}
