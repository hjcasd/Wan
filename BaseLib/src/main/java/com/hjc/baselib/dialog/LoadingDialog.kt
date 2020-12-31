package com.hjc.baselib.dialog

import android.os.Bundle
import android.view.View
import com.hjc.baselib.R
import com.hjc.baselib.databinding.DialogLoadingBinding

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:28
 * @Description: 加载框
 */
class LoadingDialog : BaseFragmentDialog<DialogLoadingBinding>(){

    companion object {

        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }

    override fun getStyleRes(): Int {
        return R.style.Dialog_Base
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
