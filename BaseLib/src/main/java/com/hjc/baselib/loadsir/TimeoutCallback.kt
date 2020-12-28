package com.hjc.baselib.loadsir

import com.hjc.baselib.R
import com.kingja.loadsir.callback.Callback

/**
 * @Author: HJC
 * @Date: 2020/5/15 11:13
 * @Description: loadSir 网络超时
 */
class TimeoutCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.default_layout_timeout
    }
}