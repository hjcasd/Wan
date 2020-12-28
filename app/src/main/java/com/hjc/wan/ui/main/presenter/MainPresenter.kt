package com.hjc.wan.ui.main.presenter

import android.Manifest
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.ui.main.MainActivity
import com.hjc.wan.ui.main.contract.MainContract
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ExplainScope

class MainPresenter : KotlinPresenter<MainContract.View>(), MainContract.Presenter {

    override fun requestPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        val activity = getView() as MainActivity
        PermissionX.init(activity)
            .permissions(*permissions)
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String?>? ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "请授予以下权限，以便程序继续运行",
                    "确定",
                    "取消"
                )
            }
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?> ->
                if (allGranted) {
                    ToastUtils.showShort("申请权限成功")
                } else {
                    ToastUtils.showShort("权限申请失败: $deniedList")
                }
            }
    }

}