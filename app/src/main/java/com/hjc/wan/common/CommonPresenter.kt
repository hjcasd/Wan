package com.hjc.wan.common

import com.blankj.utilcode.util.ToastUtils

/**
 * @Author: HJC
 * @Date: 2020/12/28 9:26
 * @Description: 通用presenter
 */
class CommonPresenter : KotlinPresenter<CommonContract.View>(), CommonContract.Presenter {

    override fun show() {
       ToastUtils.showShort("测试")
    }
}