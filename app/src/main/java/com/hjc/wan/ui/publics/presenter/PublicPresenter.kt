package com.hjc.wan.ui.publics.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.publics.contract.PublicContract

class PublicPresenter : KotlinPresenter<PublicContract.View>(), PublicContract.Presenter {

    override fun loadPublicTitles() {
        launchWrapper({
            RetrofitClient.getApi().getPublicTypes()
        }, { result ->
            if (result != null && result.size > 0) {
                getView()?.showIndicator(result)
            } else {
                ToastUtils.showShort("获取公众号分类数据失败")
            }
        })
    }
}
