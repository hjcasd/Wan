package com.hjc.baselib.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;

/**
 * @Author: HJC
 * @Date: 2019/2/21 10:54
 * @Description: 权限管理封装类
 */
public class PermissionManager {
    private Context mContext;
    private Activity mActivity;
    private Fragment mFragment;

    public PermissionManager(Context context) {
        this.mContext = context;
    }

    public PermissionManager(Activity activity) {
        this.mActivity = activity;
    }

    public PermissionManager(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * 申请权限(Context)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    public void requestPermission(PermissionCallBack callBack, String... permissions) {
        if (mContext == null) {
            return;
        }
        AndPermission.with(mContext)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(data -> callBack.onGranted())
                .onDenied(data -> callBack.onDenied())
                .start();
    }

    /**
     * 申请权限(Activity)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    public void requestPermissionInActivity(PermissionCallBack callBack, String... permissions) {
        if (mActivity == null) {
            return;
        }
        AndPermission.with(mActivity)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(data -> callBack.onGranted())
                .onDenied(data -> callBack.onDenied())
                .start();
    }

    /**
     * 申请权限(Fragment)
     *
     * @param callBack    回调
     * @param permissions 要申请的权限
     */
    public void requestPermissionInFragment(PermissionCallBack callBack, String... permissions) {
        if (mFragment == null) {
            return;
        }
        AndPermission.with(mFragment)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(data -> callBack.onGranted())
                .onDenied(data -> callBack.onDenied())
                .start();
    }

}
