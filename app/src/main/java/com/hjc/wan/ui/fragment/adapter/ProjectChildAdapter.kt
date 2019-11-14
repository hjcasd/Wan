package com.hjc.wan.ui.fragment.adapter

import android.text.Html
import android.widget.CheckBox
import android.widget.ImageView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.ui.model.ArticleBean
import com.hjc.wan.utils.image.ImageManager

class ProjectChildAdapter(data: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.item_rv_project, data) {

    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null

    override fun convert(helper: BaseViewHolder, item: ArticleBean) {
        val author = if (!StringUtils.isEmpty(item.author)) {
            item.author
        } else {
            item.shareUser
        }
        helper.setText(R.id.tvAuthor, author)

        helper.setText(R.id.tvTitle, Html.fromHtml(item.title))
        helper.setText(R.id.tvContent, Html.fromHtml(item.desc))
        helper.setText(R.id.tvTime, item.niceDate)
        helper.setText(R.id.tvChapter, item.chapterName)

        val ivPic = helper.getView<ImageView>(R.id.ivPic)
        ImageManager.loadImage(ivPic, item.envelopePic)

        val cbCollect = helper.getView<CheckBox>(R.id.cbCollect)
        cbCollect.isChecked = item.collect
        cbCollect.setOnClickListener { v ->
            mOnCollectViewClickListener?.onClick(cbCollect, helper.adapterPosition)
        }
    }


    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(checkBox: CheckBox, position: Int)
    }
}