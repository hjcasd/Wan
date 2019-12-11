package com.hjc.wan.ui.square.adapter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.ClassifyBean
import com.hjc.wan.model.SystemBean
import com.hjc.wan.utils.helper.RouterManager
import com.nex3z.flowlayout.FlowLayout

class SystemAdapter(data: MutableList<SystemBean>?) :
    BaseQuickAdapter<SystemBean, BaseViewHolder>(R.layout.item_rv_system, data) {

    override fun convert(helper: BaseViewHolder, item: SystemBean) {
        val name = item.name
        helper.setText(R.id.tv_title, name)

        val flowLayout = helper.getView<FlowLayout>(R.id.flow_layout)
        val childrenList = item.children
        initTags(childrenList, flowLayout)
    }

    /**
     * 初始化tag
     *
     * @param tagList  标签集合
     * @param flLabels 流式布局控件
     */
    private fun initTags(tagList: List<ClassifyBean>, flLabels: FlowLayout) {
        flLabels.removeAllViews()
        for (bean in tagList) {
            val view = View.inflate(mContext, R.layout.view_tree_tag, null)
            val tvTag = view.findViewById<TextView>(R.id.tv_tag)
            tvTag.text = bean.name
            flLabels.addView(tvTag)

            tvTag.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", bean.name)
                bundle.putInt("id", bean.id)
                RouterManager.jumpWithBundle(RoutePath.URL_SYSTEM_TAG, bundle)
            }
        }
    }
}