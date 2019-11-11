package com.hjc.baselib.utils.helper;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:37
 * @Description: Activity管理类
 */
public class ActivityManager {
    private static List<Activity> activityList = new LinkedList<>();

    /**
     * 添加指定的Activity
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 销毁所有的Activity
     */
    public static void finishAllActivities() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 销毁指定的Activity
     */
    public static void removeActivity(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }

    /**
     * 结束指定的Activity
     */
    private static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
        }
    }
}
