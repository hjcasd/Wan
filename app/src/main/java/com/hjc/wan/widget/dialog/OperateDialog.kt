package com.hjc.wan.widget.dialog

import android.os.Bundle
import android.view.View
import com.hjc.baselib.dialog.BaseFragmentDialog
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.databinding.DialogOperateBinding

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:28
 * @Description: 待办清单操作弹框
 */
class OperateDialog : BaseFragmentDialog<DialogOperateBinding>() {

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

    override fun initData(savedInstanceState: Bundle?) {
        arguments?.let {
            mId = it.getInt("id")
            mIsDone = it.getBoolean("isDone")

            if (mIsDone){
                mBinding.tvFinish.visibility = View.GONE
            }else{
                mBinding.tvFinish.visibility = View.VISIBLE
            }
        }
    }

    override fun addListeners() {
        mBinding.tvDelete.setOnClickListener(this)
        mBinding.tvFinish.setOnClickListener(this)
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.tv_delete -> {
                EventManager.sendEvent(MessageEvent(EventCode.DELETE_TODO, mId))
            }

            R.id.tv_finish -> {
                EventManager.sendEvent(MessageEvent(EventCode.DONE_TODO, mId))
            }
        }
        dismiss()
    }

}
