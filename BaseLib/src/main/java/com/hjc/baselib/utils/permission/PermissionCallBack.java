package com.hjc.baselib.utils.permission;

/**
 * @Author: HJC
 * @Date: 2019/7/29 14:28
 * @Description: AndPermission回调监听
 */
public interface PermissionCallBack {
    void onGranted();

    void onDenied();
}
