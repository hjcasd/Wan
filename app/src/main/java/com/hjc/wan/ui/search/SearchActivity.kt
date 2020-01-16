package com.hjc.wan.ui.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.activity.BaseMvpActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.SearchBean
import com.hjc.wan.ui.home.adapter.HomeAdapter
import com.hjc.wan.ui.search.contract.SearchContract
import com.hjc.wan.ui.search.presenter.SearchPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.SearchEditText
import kotlinx.android.synthetic.main.activity_search.*

/**
 * @Author: HJC
 * @Date: 2020/1/16 14:41
 * @Description: 搜索历史记录页面
 */
@Route(path = RoutePath.URL_SEARCH)
class SearchActivity : BaseMvpActivity<SearchContract.View, SearchPresenter>(),
    SearchContract.View {

    private lateinit var mAdapter: HomeAdapter

    private var mPage = 0

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter()
    }

    override fun createView(): SearchContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        rvList.layoutManager = manager

        mAdapter = HomeAdapter(null)
        rvList.adapter = mAdapter

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
        }
    }


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter()?.getHotKey()
    }

    override fun showHotTag(result: MutableList<SearchBean>) {
        if (result.size > 0) {
            clHot.visibility = View.VISIBLE

            flHot.removeAllViews()
            for (bean in result) {
                val view = View.inflate(this@SearchActivity, R.layout.view_navigation_tag, null)
                val tvTag = view.findViewById<TextView>(R.id.tv_tag)
                tvTag.text = bean.name
                flHot.addView(tvTag)

                tvTag.setOnClickListener { v: View? ->
                    etSearch.setText(tvTag.text.toString())
                    etSearch.requestFocus()
                    etSearch.setSelection(etSearch.text.toString().length)
                    getPresenter()?.search(mPage, tvTag.text.toString(), true)
                }
            }
        } else {
            clHot.visibility = View.GONE
        }
    }

    override fun showList(result: MutableList<ArticleBean>) {
        clHistory.visibility = View.GONE
        clHot.visibility = View.GONE
        smartRefreshLayout.visibility = View.VISIBLE
        smartRefreshLayout.finishLoadMore()

        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mAdapter.notifyDataSetChanged()
    }

    override fun finishLoadMore() {
        smartRefreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        super.addListeners()

        ivBack.setOnClickListener(this)
        tvSearch.setOnClickListener(this)
        ivClearHistory.setOnClickListener(this)

        etSearch.setOnSearchClickListener(object : SearchEditText.OnSearchClickListener {

            override fun onSearchClick(view: View?) {
                val keyword = etSearch.text.toString()
                if (StringUtils.isEmpty(keyword)) {
                    ToastUtils.showShort("请输入搜索内容")
                    return
                }
                mPage = 0
                getPresenter()?.search(mPage, keyword, true)
            }

            override fun onSearchClear() {
                clHistory.visibility = View.VISIBLE
                clHot.visibility = View.VISIBLE
                smartRefreshLayout.visibility = View.GONE
            }
        })

        smartRefreshLayout.setOnLoadMoreListener {
            mPage++
            getPresenter()?.search(mPage, etSearch.text.toString(), false)
        }

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                RouterManager.jumpToWeb(bean.title, bean.link)
            }

            setOnItemChildClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                if (!bean.collect) {
                    getPresenter()?.collectArticle(bean)
                } else {
                    getPresenter()?.unCollectArticle(bean)
                }
            }
        }
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)

        when (v?.id) {
            R.id.ivBack -> {
                KeyboardUtils.hideSoftInput(this)
                finish()
            }

            R.id.tvSearch -> {
                val keyword = etSearch.text.toString()
                if (StringUtils.isEmpty(keyword)) {
                    ToastUtils.showShort("请输入搜索内容")
                    return
                }
                mPage = 0
                getPresenter()?.search(mPage, keyword, true)
            }

            R.id.ivClearHistory -> {
                MaterialDialog(this).show {
                    cornerRadius(10f)
                    title(R.string.title)
                    message(text = "确认清除全部历史记录吗？")
                    positiveButton(text = "确定") {

                    }
                    negativeButton(R.string.cancel)
                }
            }

        }
    }

}