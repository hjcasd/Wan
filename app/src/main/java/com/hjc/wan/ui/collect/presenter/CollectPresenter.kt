package com.hjc.wan.ui.collect.presenter

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.ui.collect.contract.CollectContract

class CollectPresenter : BasePresenter<CollectContract.View>(), CollectContract.Presenter {

    override fun loadChild() {
        getView()?.showFragment()
    }

}