package com.hjc.wan.ui.presenter.home

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.ui.contract.home.HomeContract
import com.hjc.wan.ui.fragment.HomeFragment
import com.hjc.wan.ui.model.BannerBean
import com.hjc.wan.ui.model.HomeArticleBean

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun loadBannerData() {
        getView().showLoading()

        val homeFragment = getView() as HomeFragment
        RetrofitClient.getApi()
            .getBanner()
            .compose(RxHelper.bind(homeFragment))
            .subscribe(object : CommonObserver<MutableList<BannerBean>>() {

                override fun onSuccess(result: MutableList<BannerBean>?) {
                    if (result != null && result.size > 0) {
                        getView().showBanner(result)
                    } else {
                        ToastUtils.showShort("Banner数据请求失败")
                    }
                }
            })
    }

    override fun loadListData(page: Int, isFirst: Boolean) {
        if (isFirst) {
            getView().showLoading()
        }

        val homeFragment = getView() as HomeFragment
        RetrofitClient.getApi()
            .getArticle(page)
            .compose(RxHelper.bind(homeFragment))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<HomeArticleBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<HomeArticleBean>>?) {
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