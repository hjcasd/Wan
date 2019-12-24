package com.hjc.wan.ui.todo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R

class PriorityAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_rv_priority, data) {

    override fun convert(helper: BaseViewHolder, item: String?) {

        item?.let {

            helper.setText(R.id.tv_priority, item)
            when (it) {
                "重要且紧急" -> helper.setImageResource(R.id.iv_circle, R.drawable.shape_circle_red)

                "重要但不紧急" -> helper.setImageResource(R.id.iv_circle, R.drawable.shape_circle_yelllow)

                "紧急但不重要" -> helper.setImageResource(R.id.iv_circle, R.drawable.shape_circle_blue)

                "不重要不紧急" -> helper.setImageResource(R.id.iv_circle, R.drawable.shape_circle_green)

                else -> {
                }
            }
        }
    }
}