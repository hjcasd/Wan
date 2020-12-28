package com.hjc.webviewlib

import android.app.Application
import android.content.Context
import android.util.Log
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

/**
 * @Author: HJC
 * @Date: 2019/11/2 15:42
 * @Description: X5WebView初始化工具类
 */
object X5WebUtils {

    fun init(context: Context) {
        if (context is Application) {
            // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            val cb: PreInitCallback = object : PreInitCallback {
                override fun onViewInitFinished(arg0: Boolean) {
                    // x5內核初始化完成的回调，为true表示x5内核加载成功,否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("X5WebView", " onViewInitFinished is $arg0")
                }

                override fun onCoreInitFinished() {
                    Log.d("X5WebView", " onCoreInitFinished")
                }
            }
            // x5内核初始化接口
            QbSdk.initX5Environment(context, cb)
        } else {
            throw UnsupportedOperationException("context must be application...")
        }
    }

}