package com.hjc.wan.widget.dialog

import android.os.Bundle
import android.view.View
import com.hjc.baselib.dialog.BaseFragmentDialog
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import kotlinx.android.synthetic.main.dialog_operate.*

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:28
 * @Description: 待办清单操作弹框
 */
class OperateDialog : BaseFragmentDialog() {

    private var mId: Int = 0
    private var mIsDone: Boolean = false

    companion object {

        fun newInstance(id: Int, isDone: Boolean): OperateDialog {
            val dialog = OperateDialog()
            val bundle = Bundle()
            bundle.putInt("id", id)
            bundle.putBoolean("isDone", isDone)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun getStyleRes(): Int {
        return R.style.Dialog_Base
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_operate
    }

    override fun initData(savedInstanceState: Bundle?) {
        arguments?.let {
            mId = it.getInt("id")
            mIsDone = it.getBoolean("isDone")

            if (mIsDone){
                tvFinish.visibility = View.GONE
            }else{
                tvFinish.visibility = View.VISIBLE
            }
        }
    }

    override fun addListeners() {
        tvDelete.setOnClickListener(this)
        tvFinish.setOnClickListener(this)
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.tvDelete -> {
                EventManager.sendEvent(MessageEvent(EventCode.DELETE_TODO, mId))
            }

            R.id.tvFinish -> {
                EventManager.sendEvent(MessageEvent(EventCode.DONE_TODO, mId))
            }
        }
        dismiss()
    }

}
