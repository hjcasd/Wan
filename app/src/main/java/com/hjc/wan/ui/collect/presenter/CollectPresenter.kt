package com.hjc.wan.ui.collect.presenter

import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.ui.collect.contract.CollectContract

class CollectPresenter : BasePresenter<CollectContract.View>(), CollectContract.Presenter {

    override fun loadChild() {
        getView()?.showFragment()
    }

}