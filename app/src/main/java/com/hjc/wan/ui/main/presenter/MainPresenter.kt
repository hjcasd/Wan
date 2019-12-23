package com.hjc.wan.ui.main.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.utils.permission.PermissionCallBack
import com.hjc.baselib.utils.permission.PermissionManager
import com.hjc.wan.base.BasePresenter
import com.hjc.wan.ui.main.MainActivity
import com.hjc.wan.ui.main.contract.MainContract
import com.yanzhenjie.permission.runtime.Permission

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun requestPermission() {
        val activity = getView() as MainActivity

        PermissionManager(activity)
            .requestPermissionInActivity(object : PermissionCallBack {
                override fun onGranted() {
                    ToastUtils.showShort("申请存储权限成功")
                }

                override fun onDenied() {
                    ToastUtils.showShort("申请存储权限失败")
                }
            }, *Permission.Group.STORAGE)
    }

}