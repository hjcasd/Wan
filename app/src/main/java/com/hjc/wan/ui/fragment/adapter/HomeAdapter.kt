package com.hjc.wan.ui.fragment.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.ui.model.HomeArticleBean

class HomeAdapter(data: MutableList<HomeArticleBean>?) :
    BaseQuickAdapter<HomeArticleBean, BaseViewHolder>(R.layout.item_rv_home, data) {

    override fun convert(helper: BaseViewHolder, item: HomeArticleBean) {

    }
}