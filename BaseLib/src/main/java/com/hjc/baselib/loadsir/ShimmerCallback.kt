package com.hjc.baselib.loadsir

import android.content.Context
import android.view.View
import com.hjc.baselib.R
import com.kingja.loadsir.callback.Callback

/**
 * @Author: HJC
 * @Date: 2020/5/15 11:13
 * @Description: loadSir 骨架屏
 */
class ShimmerCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.default_layout_shimmer
    }

    override fun onReloadEvent(context: Context, view: View): Boolean {
        return true
    }
}