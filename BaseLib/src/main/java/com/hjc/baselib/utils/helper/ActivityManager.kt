package com.hjc.baselib.utils.helper

import android.app.Activity
import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:37
 * @Description: Activity管理类
 */
object ActivityManager {
    private val activityList: MutableList<Activity> = LinkedList()

    /**
     * 添加指定的Activity
     */
    fun addActivity(activity: Activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity)
        }
    }

    /**
     * 销毁所有的Activity
     */
    fun finishAllActivities() {
        for (activity in activityList) {
            activity.finish()
        }
    }

    /**
     * 销毁指定的Activity
     */
    fun removeActivity(cls: Class<*>) {
        var tempActivity: Activity? = null
        for (activity in activityList) {
            if (activity.javaClass == cls) {
                tempActivity = activity
            }
        }
        finishSingleActivity(tempActivity)
    }

    /**
     * 结束指定的Activity
     */
    private fun finishSingleActivity(activity: Activity?) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity)
            }
            activity.finish()
        }
    }
}