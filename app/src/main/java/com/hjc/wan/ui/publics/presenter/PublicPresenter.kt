package com.hjc.wan.ui.publics.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.model.ClassifyBean
import com.hjc.wan.ui.publics.PublicFragment
import com.hjc.wan.ui.publics.contract.PublicContract

class PublicPresenter : BasePresenter<PublicContract.View>(), PublicContract.Presenter {

    override fun loadPublicTitles() {
        val fragment = getView() as PublicFragment

        RetrofitClient.getApi()
            .getPublicTypes()
            .compose(RxHelper.bind(fragment))
            .subscribe(object : CommonObserver<MutableList<ClassifyBean>>() {

                override fun onSuccess(result: MutableList<ClassifyBean>?) {
                    if (result != null && result.size > 0) {
                        getView()?.showIndicator(result)
                    } else {
                        ToastUtils.showShort("获取公众号分类数据失败")
                    }
                }

            })
    }
}
