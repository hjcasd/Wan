package com.hjc.wan.ui.presenter.project

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.ui.contract.project.ProjectChildContract
import com.hjc.wan.ui.fragment.project.ProjectChildFragment
import com.hjc.wan.ui.model.ArticleBean

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