package com.hjc.wan.ui.square.adapter

import android.text.Html
import android.widget.CheckBox
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.ArticleBean

class PlazaAdapter(data: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.item_rv_home, data) {

    override fun convert(helper: BaseViewHolder, item: ArticleBean) {
        val author = if (!StringUtils.isEmpty(item.author)) {
            item.author
        } else {
            item.shareUser
        }
        helper.setText(R.id.tv_author, author)
        helper.setText(R.id.tv_title, Html.fromHtml(item.title))
        helper.setText(R.id.tv_time, item.niceDate)
        helper.setText(R.id.tv_chapter, item.chapterName)

        val cbCollect = helper.getView<CheckBox>(R.id.cb_collect)
        cbCollect.isChecked = item.collect
        helper.addOnClickListener(R.id.cb_collect)
    }

}