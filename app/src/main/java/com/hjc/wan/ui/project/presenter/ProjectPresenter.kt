package com.hjc.wan.ui.project.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.project.contract.ProjectContract

class ProjectPresenter : KotlinPresenter<ProjectContract.View>(), ProjectContract.Presenter {

    override fun loadProjectTitles() {
        launchWrapper({
            RetrofitClient.getApi().getProjectTypes()
        }, { result ->
            if (result != null && result.size > 0) {
                getView()?.showIndicator(result)
            } else {
                ToastUtils.showShort("获取项目分类数据失败")
            }
        })
    }

}