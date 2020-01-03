package com.hjc.baselib.widget.bar

import android.view.View

/**
 * @Author: HJC
 * @Date: 2019/7/29 14:25
 * @Description: TitleBar左右点击事件
 */
interface OnBarClickListener {
    fun leftClick(view: View)
    fun rightClick(view: View)
}