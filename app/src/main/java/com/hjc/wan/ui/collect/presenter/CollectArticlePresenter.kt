package com.hjc.wan.ui.collect.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.CollectArticleBean
import com.hjc.wan.ui.collect.child.CollectArticleFragment
import com.hjc.wan.ui.collect.contract.CollectArticleContract

class CollectArticlePresenter : BasePresenter<CollectArticleContract.View>(),
    CollectArticleContract.Presenter {

    override fun loadListData(page: Int) {
        val fragment = getView() as CollectArticleFragment

        RetrofitClient.getApi()
            .getCollectArticle(page)
            .compose(RxHelper.bind(fragment))
            .subscribe(object :
                CommonObserver<BasePageResponse<MutableList<CollectArticleBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<CollectArticleBean>>?) {
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
     * 取消收藏
     */
    override fun unCollectArticle(bean: CollectArticleBean, position: Int) {
        val fragment = getView() as CollectArticleFragment

        RetrofitClient.getApi()
            .unCollectOrigin(bean.id, bean.originId)
            .compose(RxHelper.bind(fragment))
            .subscribe(object : ProgressObserver<Any>(fragment.childFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("已取消收藏")
                    getView()?.showUnCollectList(position)
                }

            })
    }

}