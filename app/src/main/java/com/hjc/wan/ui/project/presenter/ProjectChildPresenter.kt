package com.hjc.wan.ui.project.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.project.child.ProjectChildFragment
import com.hjc.wan.ui.project.contract.ProjectChildContract

class ProjectChildPresenter : BasePresenter<ProjectChildContract.View>(),
    ProjectChildContract.Presenter {

    /**
     * 加载列表
     */
    override fun loadListData(page: Int, cid: Int) {
        val fragment = getView() as ProjectChildFragment

        RetrofitClient.getApi()
            .getProjectDataByType(page, cid)
            .compose(RxHelper.bind(fragment))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<ArticleBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<ArticleBean>>?) {
                    val data = result?.datas
                    data?.let {
                        if (data.size > 0) {
                            getView()?.showContent()
                            getView()?.showList(data)
                        } else {
                            if (page == 0) {
                                getView()?.showEmpty()
                            } else {
                                getView()?.showContent()
                                ToastUtils.showShort("没有更多数据了")
                            }
                        }
                    }
                }

                override fun onFailure(errorMsg: String) {
                    super.onFailure(errorMsg)
                    if (errorMsg == "网络不可用" || errorMsg == "请求网络超时") {
                        getView()?.showNoNetwork()
                    } else {
                        getView()?.showError()
                    }
                }

            })
    }

    /**
     * 收藏
     */
    override fun collectArticle(bean : ArticleBean) {
        val fragment = getView() as ProjectChildFragment

        RetrofitClient.getApi()
            .collect(bean.id)
            .compose(RxHelper.bind(fragment))
            .subscribe(object : ProgressObserver<Any>(fragment.childFragmentManager){

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("收藏成功")
                    getView()?.showCollectList(bean)
                }

            })
    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean : ArticleBean) {
        val fragment = getView() as ProjectChildFragment

        RetrofitClient.getApi()
            .unCollect(bean.id)
            .compose(RxHelper.bind(fragment))
            .subscribe(object : ProgressObserver<Any>(fragment.childFragmentManager){

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("已取消收藏")
                    getView()?.showUnCollectList(bean)
                }

            })
    }

}