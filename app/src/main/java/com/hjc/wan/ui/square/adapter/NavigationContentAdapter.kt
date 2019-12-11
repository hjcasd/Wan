package com.hjc.wan.ui.square.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjc.wan.R
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.NavigationBean
import com.hjc.wan.utils.helper.RouterManager
import com.nex3z.flowlayout.FlowLayout

class NavigationContentAdapter(data: MutableList<NavigationBean>?) :
    BaseQuickAdapter<NavigationBean, BaseViewHolder>(R.layout.item_rv_navigation_content, data) {

    override fun convert(helper: BaseViewHolder, item: NavigationBean) {
        val name = item.name
        helper.setText(R.id.tv_chapter_name, name)

        val flowLayout = helper.getView<FlowLayout>(R.id.flow_layout)

        val articleList = item.articles
        initTags(articleList, flowLayout)
    }

    /**
     * 初始化tag
     *
     * @param tagList  标签集合
     * @param flLabels 流式布局控件
     */
    private fun initTags(tagList: List<ArticleBean>, flLabels: FlowLayout) {
        flLabels.removeAllViews()
        for (bean in tagList) {
            val view = View.inflate(mContext, R.layout.view_navigation_tag, null)
            val tvTag = view.findViewById<TextView>(R.id.tv_tag)
            tvTag.text = bean.title
            flLabels.addView(tvTag)

            tvTag.setOnClickListener {
                RouterManager.jumpToWeb(bean.title, bean.link)
            }
        }
    }

}