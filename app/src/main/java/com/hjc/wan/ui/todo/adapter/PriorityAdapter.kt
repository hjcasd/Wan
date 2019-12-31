package com.hjc.wan.ui.todo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.CircleColorView

class PriorityAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_rv_priority, data) {

    override fun convert(helper: BaseViewHolder, item: String?) {
        item?.let {
            helper.setText(R.id.tv_priority, item)

            val colorView = helper.getView<CircleColorView>(R.id.color_view)
            colorView.setViewColor(SettingManager.getColorByType(helper.adapterPosition))
        }
    }
}