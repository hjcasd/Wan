package com.hjc.wan.ui.square.presenter

import com.hjc.wan.base.BasePresenter
import com.hjc.wan.ui.square.contract.SquareContract

class SquarePresenter : BasePresenter<SquareContract.View>(), SquareContract.Presenter {

    override fun getSquareTitles() {
        val titleList = mutableListOf<String>()
        titleList.add("广场")
        titleList.add("体系")
        titleList.add("导航")

        getView().showIndicator(titleList)
    }

}