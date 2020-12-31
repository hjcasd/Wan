package com.hjc.wan.ui.main.contract

import com.hjc.baselib.base.IBaseView

interface MainContract {

    interface View : IBaseView {
        override fun startLoading() {
        }

        override fun dismissLoading() {
        }

        override fun showLoading() {

        }

        override fun showContent() {
        }

        override fun showEmpty() {
        }

        override fun showError() {
        }
    }

    interface Presenter {
        fun requestPermission()
    }
}