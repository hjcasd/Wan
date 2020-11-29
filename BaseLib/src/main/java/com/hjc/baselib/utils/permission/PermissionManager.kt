package com.hjc.baselib.utils.permission

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.yanzhenjie.permission.AndPermission

/**
 * @Author: HJC
 * @Date: 2019/2/21 10:54
 * @Description: 权限管理封装类
 */
class PermissionManager {
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null

    constructor(context: Context?) {
        mContext = context
    }

    constructor(activity: Activity?) {
        mActivity = activity
    }

    constructor(fragment: Fragment?) {
        mFragment = fragment
    }

    /**
     * 申请权限(Context)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    fun requestPermission(callBack: PermissionCallBack, vararg permissions: String?) {
        if (mContext == null) {
            return
        }
        AndPermission.with(mContext)
            .runtime()
            .permission(permissions)
            .rationale(RuntimeRationale())
            .onGranted { data: List<String?>? -> callBack.onGranted() }
            .onDenied { data: List<String?>? -> callBack.onDenied() }
            .start()
    }

    /**
     * 申请权限(Activity)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    fun requestPermissionInActivity(callBack: PermissionCallBack, vararg permissions: String?) {
        if (mActivity == null) {
            return
        }
        AndPermission.with(mActivity)
            .runtime()
            .permission(permissions)
            .rationale(RuntimeRationale())
            .onGranted { data: List<String?>? -> callBack.onGranted() }
            .onDenied { data: List<String?>? -> callBack.onDenied() }
            .start()
    }

    /**
     * 申请权限(Fragment)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    fun requestPermissionInFragment(callBack: PermissionCallBack, vararg permissions: String?) {
        if (mFragment == null) {
            return
        }
        AndPermission.with(mFragment)
            .runtime()
            .permission(permissions)
            .rationale(RuntimeRationale())
            .onGranted { data: List<String?>? -> callBack.onGranted() }
            .onDenied { data: List<String?>? -> callBack.onDenied() }
            .start()
    }
}