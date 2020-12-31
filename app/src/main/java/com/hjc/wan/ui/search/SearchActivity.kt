package com.hjc.wan.ui.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivitySearchBinding
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.SearchBean
import com.hjc.wan.model.db.History
import com.hjc.wan.model.db.HistoryDataBase
import com.hjc.wan.ui.home.adapter.HomeAdapter
import com.hjc.wan.ui.search.contract.SearchContract
import com.hjc.wan.ui.search.presenter.SearchPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.SearchEditText

/**
 * @Author: HJC
 * @Date: 2020/1/16 14:41
 * @Description: 搜索历史记录页面
 */
@Route(path = RoutePath.URL_SEARCH)
class SearchActivity : BaseActivity<ActivitySearchBinding, SearchContract.View, SearchPresenter>(),
    SearchContract.View {

    private lateinit var mAdapter: HomeAdapter

    private var mPage = 0

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter()
    }

    override fun createView(): SearchContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(this)
        mBinding.rvList.layoutManager = manager

        mAdapter = HomeAdapter(null)
        mBinding.rvList.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }

        mBinding.clSearch.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        getPresenter().getHotKey()
        getPresenter().getHistory()
    }

    override fun showHotTag(result: MutableList<SearchBean>) {
        if (result.size > 0) {
            mBinding.clHot.visibility = View.VISIBLE

            mBinding.flHot.removeAllViews()
            for (bean in result) {
                val view = View.inflate(this@SearchActivity, R.layout.view_navigation_tag, null)
                val tvTag = view.findViewById<TextView>(R.id.tv_tag)
                tvTag.text = bean.name
                mBinding.flHot.addView(tvTag)

                tvTag.setOnClickListener {
                    mBinding.etSearch.setText(tvTag.text.toString())
                    mBinding.etSearch.requestFocus()
                    mBinding.etSearch.setSelection( mBinding.etSearch.text.toString().length)

                    mPage = 0
                    getPresenter().search(mPage, tvTag.text.toString(), true)
                }
            }
        } else {
            mBinding.clHot.visibility = View.GONE
        }
    }

    override fun showList(result: MutableList<ArticleBean>) {
        mBinding.clHistory.visibility = View.GONE
        mBinding.clHot.visibility = View.GONE
        mBinding.refreshLayout.visibility = View.VISIBLE
        mBinding.refreshLayout.finishLoadMore()

        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun showHistory(result: List<History>) {
        mBinding.clHistory.visibility = View.VISIBLE
        mBinding.flHistory.removeAllViews()

        for (history in result) {
            val view = View.inflate(this@SearchActivity, R.layout.view_navigation_tag, null)
            val tvTag = view.findViewById<TextView>(R.id.tv_tag)
            tvTag.text = history.name
            mBinding.flHistory.addView(tvTag)

            tvTag.setOnClickListener {
                mBinding.etSearch.setText(tvTag.text.toString())
                mBinding.etSearch.requestFocus()
                mBinding.etSearch.setSelection( mBinding.etSearch.text.toString().length)
                getPresenter().search(mPage, tvTag.text.toString(), true)
            }
        }
    }

    override fun hideHistory() {
        mBinding.clHistory.visibility = View.GONE
    }

    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mAdapter.notifyDataSetChanged()
    }

    override fun refreshComplete() {
        mBinding.refreshLayout.finishLoadMore()
    }

    override fun addListeners() {
        mBinding.ivBack.setOnClickListener(this)
        mBinding.tvSearch.setOnClickListener(this)
        mBinding.ivClearHistory.setOnClickListener(this)

        mBinding.etSearch.setOnSearchClickListener(object : SearchEditText.OnSearchClickListener {

            override fun onSearchClick(view: View?) {
                val keyword =  mBinding.etSearch.text.toString()
                if (StringUtils.isEmpty(keyword)) {
                    ToastUtils.showShort("请输入搜索内容")
                    return
                }
                mPage = 0
                getPresenter().search(mPage, keyword, true)
            }

            override fun onSearchClear() {
                mBinding.clHistory.visibility = View.VISIBLE
                getPresenter().getHistory()
                mBinding.clHot.visibility = View.VISIBLE
                mBinding.refreshLayout.visibility = View.GONE
            }
        })

        mBinding.refreshLayout.setOnLoadMoreListener {
            mPage++
            getPresenter().search(mPage,  mBinding.etSearch.text.toString(), false)
        }

        mAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                RouterManager.jumpToWeb(bean.title, bean.link)
            }

            setOnItemChildClickListener { _, _, position ->
                val bean = mAdapter.data[position]
                if (!bean.collect) {
                    getPresenter().collectArticle(bean)
                } else {
                    getPresenter().unCollectArticle(bean)
                }
            }
        }
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                KeyboardUtils.hideSoftInput(this)
                finish()
            }

            R.id.tv_search -> {
                val keyword =  mBinding.etSearch.text.toString()
                if (StringUtils.isEmpty(keyword)) {
                    ToastUtils.showShort("请输入搜索内容")
                    return
                }
                mPage = 0
                getPresenter().search(mPage, keyword, true)
            }

            R.id.iv_clear_history -> {
                MaterialDialog(this).show {
                    cornerRadius(10f)
                    title(R.string.app_tip)
                    message(text = "确认清除全部历史记录吗？")
                    positiveButton(text = "确定") {
                        HistoryDataBase.getInstance(this@SearchActivity).getHistoryDao().deleteAll()
                        mBinding.clHistory.visibility = View.GONE
                    }
                    negativeButton(R.string.app_cancel)
                }
            }
        }
    }

}