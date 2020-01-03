package com.hjc.wan.ui.project.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.ClassifyBean

interface ProjectContract {

    interface View : IBaseView {
        fun showIndicator(classifyList: MutableList<ClassifyBean>)
    }

    interface Presenter {
        fun loadProjectTitles()
    }
}
