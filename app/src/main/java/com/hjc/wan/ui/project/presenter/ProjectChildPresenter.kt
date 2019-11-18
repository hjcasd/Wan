package com.hjc.wan.ui.project.presenter

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.project.child.ProjectChildFragment
import com.hjc.wan.ui.project.contract.ProjectChildContract

class ProjectChildPresenter : BasePresenter<ProjectChildContract.View>(),
    ProjectChildContract.Presenter {

    override fun loadListData(page: Int, cid: Int) {
        val projectChildFragment = getView() as ProjectChildFragment
        RetrofitClient.getApi()
            .getProjectDataByType(page, cid)
            .compose(RxHelper.bind(projectChildFragment))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<ArticleBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<ArticleBean>>?) {
                    val data = result?.datas
                    if (data != null && data.size > 0) {
                        getView().showContent()
                        getView().showList(data)
                    } else {
                        getView().showEmpty()
                    }
                }

                override fun onFailure(errorMsg: String) {
                    super.onFailure(errorMsg)
                    if (errorMsg == "网络不可用" || errorMsg == "请求网络超时") {
                        getView().showNoNetwork()
                    } else {
                        getView().showError()
                    }
                }

            })
    }

}