package com.hjc.wan.ui.square.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpListActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.ui.square.adapter.SystemTagAdapter
import com.hjc.wan.ui.square.contract.SystemTagContract
import com.hjc.wan.ui.square.presenter.SystemTagPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_common.*

/**
 * @Author: HJC
 * @Date: 2019/12/11 16:38
 * @Description: 体系tag下的文章列表页面
 */
@Route(path = RoutePath.URL_SYSTEM_TAG)
class SystemTagActivity : BaseMvpListActivity<SystemTagContract.View, SystemTagPresenter>(),
    SystemTagContract.View {

    private lateinit var mAdapter: SystemTagAdapter

    @Autowired(name = "params")
    @JvmField
    var bundle: Bundle? = null

    private var mCid = 0

    private var mPage = 0

    override fun createPresenter(): SystemTagPresenter {
        return SystemTagPresenter()
    }

    override fun createView(): SystemTagContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_common
    }

    override fun initView() {
        super.initView()
        val manager = LinearLayoutManager(this)
        rvCommon.layoutManager = manager

        mAdapter = SystemTagAdapter(null)
        rvCommon.adapter = mAdapter

        SettingManager.getListAnimationType().let {
            if (it != 0) {
                mAdapter.openLoadAnimation(it)
            } else {
                mAdapter.closeLoadAnimation()
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
            .init()
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.setBgColor(SettingManager.getThemeColor())
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        bundle?.let {
            val title = it.getString("title")
            val cid = it.getInt("id", 0)

            titleBar.setTitle(title)
            mCid = cid

            showLoading()
            getPresenter()?.loadListData(mPage, mCid)
        }
    }


    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        super.addListeners()

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter()?.loadListData(mPage, mCid)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage, mCid)
            }

        })

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

    override fun showCollectList(bean: ArticleBean) {
        bean.collect = true
        mAdapter.notifyDataSetChanged()
    }

    override fun showUnCollectList(bean: ArticleBean) {
        bean.collect = false
        mAdapter.notifyDataSetChanged()
    }

}