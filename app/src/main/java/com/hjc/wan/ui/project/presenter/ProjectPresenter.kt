package com.hjc.wan.ui.project.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.ClassifyBean
import com.hjc.wan.ui.project.ProjectFragment
import com.hjc.wan.ui.project.contract.ProjectContract

class ProjectPresenter : BasePresenter<ProjectContract.View>(), ProjectContract.Presenter {

    override fun loadProjectTitles() {
        val projectFragment = getView() as ProjectFragment

        RetrofitClient.getApi()
            .getProjectTypes()
            .compose(RxHelper.bind(projectFragment))
            .subscribe(object : CommonObserver<MutableList<ClassifyBean>>() {

                override fun onSuccess(result: MutableList<ClassifyBean>?) {
                    if (result != null && result.size > 0) {
                        getView()?.showIndicator(result)
                    } else {
                        ToastUtils.showShort("获取项目分类数据失败")
                    }
                }

            })
    }

}