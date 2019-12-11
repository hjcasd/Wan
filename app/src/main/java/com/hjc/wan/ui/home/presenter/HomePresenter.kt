package com.hjc.wan.ui.home.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.BannerBean
import com.hjc.wan.ui.home.HomeFragment
import com.hjc.wan.ui.home.contract.HomeContract

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    /**
     * 加载banner
     */
    override fun loadBannerData() {
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

    /**
     * 加载列表
     */
    override fun loadListData(page: Int) {
        val homeFragment = getView() as HomeFragment
        RetrofitClient.getApi()
            .getArticle(page)
            .compose(RxHelper.bind(homeFragment))
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

    /**
     * 收藏
     */
    override fun collectArticle(bean : ArticleBean) {
        val homeFragment = getView() as HomeFragment
        RetrofitClient.getApi()
            .collect(bean.id)
            .compose(RxHelper.bind(homeFragment))
            .subscribe(object : ProgressObserver<Any>(homeFragment.childFragmentManager){

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("收藏成功")
                    getView().showCollectList(bean)
                }

            })
    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean : ArticleBean) {
        val homeFragment = getView() as HomeFragment
        RetrofitClient.getApi()
            .unCollect(bean.id)
            .compose(RxHelper.bind(homeFragment))
            .subscribe(object : ProgressObserver<Any>(homeFragment.childFragmentManager){

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("已取消收藏")
                    getView().showUnCollectList(bean)
                }

            })
    }

}