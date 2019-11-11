package com.hjc.wan.http.observer

import com.blankj.utilcode.util.ToastUtils

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:52
 * @Description: 通用的Observer
 */
abstract class CommonObserver<T> : BaseObserver<T>() {

    override fun onFailure(errorMsg: String) {
        ToastUtils.showShort(errorMsg)
    }

}
