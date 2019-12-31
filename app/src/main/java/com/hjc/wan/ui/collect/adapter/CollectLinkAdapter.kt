package com.hjc.wan.ui.collect.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.CollectLinkBean

class CollectLinkAdapter(data: MutableList<CollectLinkBean>?) :
    BaseQuickAdapter<CollectLinkBean, BaseViewHolder>(R.layout.item_rv_link, data) {

    override fun convert(helper: BaseViewHolder, item: CollectLinkBean?) {
        item?.let {
            helper.setText(R.id.tv_name, it.name)
            helper.setText(R.id.tv_link, it.link)
        }
    }
}
