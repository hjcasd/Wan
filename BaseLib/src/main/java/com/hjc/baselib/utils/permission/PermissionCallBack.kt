package com.hjc.baselib.utils.permission

/**
 * @Author: HJC
 * @Date: 2020/1/3 18:06
 * @Description: AndPermission回调监听
 */
interface PermissionCallBack {
    fun onGranted()
    fun onDenied()
}