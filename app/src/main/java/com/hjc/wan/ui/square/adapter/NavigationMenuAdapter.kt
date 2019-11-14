package com.hjc.wan.ui.square.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R

class NavigationMenuAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_rv_navigation_menu, data) {

    private var listener: OnSelectListener? = null
    private var selectedPosition = -1

    override fun convert(helper: BaseViewHolder, item: String) {
        val tvChapter = helper.getView<TextView>(R.id.tv_chapter)
        tvChapter.text = item

        val position = helper.adapterPosition
        tvChapter.isSelected = selectedPosition == position

        if (listener != null) {
            tvChapter.setOnClickListener {
                setSelection(position)
                listener?.onSelected(position)
            }
        }
    }

    fun setSelection(position: Int) {
        this.selectedPosition = position
        notifyDataSetChanged()
    }

    interface OnSelectListener {
        fun onSelected(position: Int)
    }

    fun setOnSelectListener(listener: OnSelectListener) {
        this.listener = listener
    }
}