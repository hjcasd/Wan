package com.hjc.wan.ui.todo.adapter

import android.graphics.Color
import android.widget.ImageView
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.TodoBean
import com.hjc.wan.utils.CommonUtils

class TodoAdapter(data: MutableList<TodoBean>?) :
    BaseQuickAdapter<TodoBean, BaseViewHolder>(R.layout.item_rv_to_do, data) {

    override fun convert(helper: BaseViewHolder, item: TodoBean?) {
        item?.let {
            helper.setText(R.id.tv_title, it.title)
            helper.setText(R.id.tv_time, it.dateStr)
            helper.setText(R.id.tv_content, it.content)


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
            helper.getView<ImageView>(R.id.iv_tag).imageTintList =
                CommonUtils.getOneColorStateList(getColorByType(it.priority))
        }
    }

    private fun getColorByType(type: Int): Int {
        var color = Color.parseColor("#fe7b7b")
        when (type) {
            0 -> color = Color.parseColor("#fe7b7b")
            1 -> color = Color.parseColor("#dda239")
            2 -> color = Color.parseColor("#69adef")
            3 -> color = Color.parseColor("#6cc959")
        }
        return color
    }

}