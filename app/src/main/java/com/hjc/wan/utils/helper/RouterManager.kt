package com.hjc.wan.utils.helper

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.utils.ClickUtils
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath

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


    /**
     * 页面跳转
     * @param title 标题
     * @param url 链接地址
     */
    fun jumpToWeb(title: String, url: String) {
        if (ClickUtils.isFastClick()) {
            ToastUtils.showShort("点的太快了,歇会呗!")
            return
        }
        if (StringUtils.isEmpty(url)) {
            ToastUtils.showShort("链接地址不能为空")
            return
        }

        ARouter.getInstance()
            .build(RoutePath.URL_WEB)
            .withString("title", title)
            .withString("url", url)
            .withTransition(R.anim.slide_enter_bottom, R.anim.slide_exit_bottom)
            .navigation()
    }
}