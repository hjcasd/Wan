package com.hjc.wan.ui.collect.adapter

import android.text.Html
import android.widget.ImageView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.hjc.wan.R
import com.hjc.wan.model.CollectArticleBean
import com.hjc.wan.utils.image.ImageManager

class CollectArticleAdapter(data: MutableList<CollectArticleBean>?) :
    BaseQuickAdapter<CollectArticleBean, BaseViewHolder>(R.layout.item_rv_home, data) {

    companion object{
        private const val TYPE_TEXT = 1
        private const val TYPE_IMAGE = 2
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<CollectArticleBean>() {
            override fun getItemType(articleBean: CollectArticleBean): Int {
                return if (StringUtils.isEmpty(articleBean.envelopePic)) TYPE_TEXT else TYPE_IMAGE
            }
        }

        multiTypeDelegate
            .registerItemType(TYPE_TEXT, R.layout.item_rv_home)
            .registerItemType(TYPE_IMAGE, R.layout.item_rv_project)
    }

    override fun convert(helper: BaseViewHolder, item: CollectArticleBean) {
        val author = if (!StringUtils.isEmpty(item.author)) {
            item.author
        } else {
            "匿名用户"
        }
        helper.setText(R.id.tv_author, author)
        helper.setText(R.id.tv_title, Html.fromHtml(item.title))
        helper.setText(R.id.tv_time, item.niceDate)
        helper.setText(R.id.tv_chapter, item.chapterName)

        helper.setChecked(R.id.cb_collect, true)
        helper.addOnClickListener(R.id.cb_collect)

        if (helper.itemViewType == TYPE_IMAGE) {
            helper.setText(R.id.tv_content, Html.fromHtml(item.desc))

            val ivPic = helper.getView<ImageView>(R.id.iv_pic)
            ImageManager.loadImage(ivPic, item.envelopePic)
        }
    }
}
