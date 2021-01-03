package com.hjc.wan.ui.todo.adapter

import android.widget.ImageView
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.TodoBean
import com.hjc.wan.utils.helper.SettingManager

class TodoAdapter(data: MutableList<TodoBean>?) :
    BaseQuickAdapter<TodoBean, BaseViewHolder>(R.layout.item_rv_to_do, data) {

    override fun convert(helper: BaseViewHolder, item: TodoBean?) {
        item?.let {
            helper.setText(R.id.tv_title, it.title)
            helper.setText(R.id.tv_time, it.dateStr)
            helper.setText(R.id.tv_content, it.content)
            helper.addOnClickListener(R.id.iv_more)

            if (it.status == 1) {
                //已完成
                helper.setVisible(R.id.iv_state, true)
                helper.setImageResource(R.id.iv_state, R.mipmap.icon_done)

            } else {
                if (it.date < TimeUtils.getNowMills()) {
                    //未完成并且超过了预定完成时间
                    helper.setVisible(R.id.iv_state, true)
                    helper.setImageResource(R.id.iv_state, R.mipmap.icon_expired)
                } else {
                    //未完成
                    helper.setVisible(R.id.iv_state, false)
                }
            }
            helper.getView<ImageView>(R.id.iv_tag).imageTintList = SettingManager.getOneColorStateList(SettingManager.getColorByType(it.priority))
        }
    }

}