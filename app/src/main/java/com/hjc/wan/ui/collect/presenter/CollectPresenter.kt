package com.hjc.wan.ui.collect.presenter

import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.ui.collect.contract.CollectContract

class CollectPresenter : KotlinPresenter<CollectContract.View>(), CollectContract.Presenter {

    override fun loadChild() {
        getView()?.showFragment()
    }

}