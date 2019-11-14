package com.hjc.wan.ui.fragment.home.adapter

import android.text.Html
import android.widget.CheckBox
import android.widget.ImageView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.hjc.wan.R
import com.hjc.wan.ui.model.ArticleBean
import com.hjc.wan.utils.image.ImageManager

class HomeAdapter(data: MutableList<ArticleBean>?) :
    BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.item_rv_home, data) {

    private val TYPE_TEXT = 1
    private val TYPE_IMAGE_ONE = 2

    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null

    init {
        multiTypeDelegate = object : MultiTypeDelegate<ArticleBean>() {
            override fun getItemType(articleBean: ArticleBean): Int {
                return if (StringUtils.isEmpty(articleBean.envelopePic)) TYPE_TEXT else TYPE_IMAGE_ONE
            }
        }

        multiTypeDelegate
            .registerItemType(TYPE_TEXT, R.layout.item_rv_home)
            .registerItemType(TYPE_IMAGE_ONE, R.layout.item_rv_project)
    }

    override fun convert(helper: BaseViewHolder, item: ArticleBean) {
        when (helper.itemViewType) {
            TYPE_TEXT -> initType1(helper, item)

            TYPE_IMAGE_ONE -> initType2(helper, item)
        }
    }

    private fun initType1(helper: BaseViewHolder, item: ArticleBean) {
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
        cbCollect.setOnClickListener { v ->
            mOnCollectViewClickListener?.onClick(cbCollect, helper.adapterPosition)
        }
    }

    private fun initType2(helper: BaseViewHolder, item: ArticleBean) {
        val author = if (!StringUtils.isEmpty(item.author)) {
            item.author
        } else {
            item.shareUser
        }
        helper.setText(R.id.tv_author, author)

        helper.setText(R.id.tv_title, Html.fromHtml(item.title))
        helper.setText(R.id.tv_content, Html.fromHtml(item.desc))
        helper.setText(R.id.tv_time, item.niceDate)
        helper.setText(R.id.tv_chapter, item.chapterName)

        val ivPic = helper.getView<ImageView>(R.id.iv_pic)
        ImageManager.loadImage(ivPic, item.envelopePic)

        val cbCollect = helper.getView<CheckBox>(R.id.cb_collect)
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