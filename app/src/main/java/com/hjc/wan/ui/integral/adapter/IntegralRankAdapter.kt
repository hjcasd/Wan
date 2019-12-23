package com.hjc.wan.ui.integral.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.IntegralBean

class IntegralRankAdapter(data: MutableList<IntegralBean>?) :
    BaseQuickAdapter<IntegralBean, BaseViewHolder>(R.layout.item_rv_integral_rank, data) {

    override fun convert(helper: BaseViewHolder, item: IntegralBean) {
        helper.setText(R.id.tv_ranking, "${helper.adapterPosition + 1}")
        helper.setText(R.id.tv_name, item.username)
        helper.setText(R.id.tv_score, item.coinCount.toString())
    }

}