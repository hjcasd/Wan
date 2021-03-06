package com.hjc.wan

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.hjc.baselib.loadsir.*
import com.hjc.wan.application.MyActivityLifecycleCallbacks
import com.hjc.wan.utils.BuglyUtils
import com.hjc.webviewlib.X5WebUtils
import com.kingja.loadsir.core.LoadSir


/**
 * @Author: HJC
 * @Date: 2019/1/4 14:46
 * @Description: application
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initUtils()
        initARouter()
        initLoadSir()
        BuglyUtils.init(this)
        X5WebUtils.init(this)
        registerActivityLifecycleCallbacks(MyActivityLifecycleCallbacks())
    }


    /**
     * 初始化工具类
     */
    private fun initUtils() {
        Utils.init(this)
        val config = LogUtils.getConfig()
        config.isLogSwitch = BuildConfig.IS_DEBUG
        config.globalTag = "tag"
    }

    /**
     * 初始化路由
     */
    private fun initARouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.IS_DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(TimeoutCallback())
            .addCallback(ShimmerCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }
}
