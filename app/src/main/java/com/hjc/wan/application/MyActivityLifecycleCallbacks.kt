package com.hjc.wan.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.hjc.baselib.utils.helper.ActivityHelper

/**
 * @Author: HJC
 * @Date: 2020/12/31 14:20
 * @Description: 全局生命周期管理
 */
class MyActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityHelper.addActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityHelper.removeActivity(activity::class.java)
    }
}