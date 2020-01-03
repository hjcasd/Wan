package com.hjc.wan.widget.dialog

import android.os.Bundle
import android.view.View
import com.hjc.baselib.dialog.BaseDialog
import com.hjc.wan.R

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:28
 * @Description: 加载框
 */
class LoadingDialog : BaseDialog() {

    companion object {

        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }

    override fun getStyleRes(): Int {
        return R.style.Dialog_Base
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun initData(savedInstanceState: Bundle?) {
        //去掉遮盖层
        val window = dialog?.window
        window?.setDimAmount(0f)
        isCancelable = false
    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

}
