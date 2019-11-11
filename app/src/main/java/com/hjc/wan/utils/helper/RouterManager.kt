package com.hjc.wan.utils.helper

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.hjc.wan.R

object RouterManager {

    /**
     * 页面跳转
     * @param path 要跳转页面对应的路由url
     */
    fun jump(path: String) {
        ARouter.getInstance()
            .build(path)
            .withTransition(R.anim.slide_enter_bottom, R.anim.slide_exit_bottom)
            .navigation()
    }


    /**
     * 页面跳转
     * @param path 要跳转页面对应的路由url
     * @param context 当前页面
     * @param compat 转场动画
     */
    fun jumpWithScene(path: String, context: Context, compat: ActivityOptionsCompat) {
        ARouter.getInstance()
            .build(path)
            .withOptionsCompat(compat)
            .navigation(context)
    }
}