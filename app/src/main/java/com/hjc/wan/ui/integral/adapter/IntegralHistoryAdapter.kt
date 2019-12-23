package com.hjc.wan.ui.integral.adapter

import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.IntegralHistoryBean

class IntegralHistoryAdapter(data: MutableList<IntegralHistoryBean>?) :
    BaseQuickAdapter<IntegralHistoryBean, BaseViewHolder>(R.layout.item_rv_integral_history, data) {

    override fun convert(helper: BaseViewHolder, item: IntegralHistoryBean?) {
        item?.let {
            val descStr = if (it.desc.contains("积分")) it.desc.subSequence(
                it.desc.indexOf("积分"),
                it.desc.length
            ) else ""

            helper.setText(R.id.tv_desc, it.reason + descStr)
            helper.setText(R.id.tv_time, TimeUtils.millis2String(it.date))
            helper.setText(R.id.tv_score, "+${it.coinCount}")
        }
    }

}