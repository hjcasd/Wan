package com.hjc.wan.ui.search.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.SearchBean
import com.hjc.wan.ui.search.SearchActivity
import com.hjc.wan.ui.search.contract.SearchContract

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun getHotKey() {
        val activity = getView() as SearchActivity

        RetrofitClient.getApi()
            .getHotKey()
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<MutableList<SearchBean>>(activity.supportFragmentManager) {

                override fun onSuccess(result: MutableList<SearchBean>?) {
                    if (result != null) {
                        getView()?.showHotTag(result)
                    } else {
                        ToastUtils.showShort("获取热门搜索数据失败")
                    }
                }

            })

    }

    override fun search(page: Int, key: String, isShow: Boolean) {
        val activity = getView() as SearchActivity

        RetrofitClient.getApi()
            .search(page, key)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<BasePageResponse<MutableList<ArticleBean>>>(activity.supportFragmentManager, isShow) {

                override fun onSuccess(result: BasePageResponse<MutableList<ArticleBean>>?) {
                    val data = result?.datas
                    data?.let {
                        if (data.size > 0) {
                            getView()?.showList(data)
                        } else {
                            if (page == 0) {
                                ToastUtils.showShort("暂无搜索数据")
                            } else {
                                ToastUtils.showShort("没有更多数据了")
                                getView()?.finishLoadMore()
                            }
                        }
                    }
                }

            })
    }


    /**
     * 收藏
     */
    override fun collectArticle(bean: ArticleBean) {
        val activity = getView() as SearchActivity

        RetrofitClient.getApi()
            .collect(bean.id)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("收藏成功")
                    getView()?.showCollectList(bean)
                }

            })
    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean: ArticleBean) {
        val activity = getView() as SearchActivity

        RetrofitClient.getApi()
            .unCollect(bean.id)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("已取消收藏")
                    getView()?.showUnCollectList(bean)
                }

            })
    }

}