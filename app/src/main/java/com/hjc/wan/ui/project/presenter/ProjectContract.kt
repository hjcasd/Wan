package com.hjc.wan.ui.project.presenter

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.ClassifyBean

interface ProjectContract {
    interface View : IBaseView {
        fun showIndicator(classifyList: MutableList<ClassifyBean>)
    }

    interface Presenter {
        fun getProjectTitles()
    }
}
