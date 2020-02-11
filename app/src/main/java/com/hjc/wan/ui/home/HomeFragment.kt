package com.hjc.wan.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseMvpFragment
import com.hjc.baselib.widget.bar.OnBarRightClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.BannerBean
import com.hjc.wan.ui.home.adapter.HomeAdapter
import com.hjc.wan.ui.home.contract.HomeContract
import com.hjc.wan.ui.home.presenter.HomePresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.utils.image.GlideImageLoader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_common.*
import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:35
 * @Description: 首页
 */
class HomeFragment : BaseMvpFragment<HomeContract.View, HomePresenter>(), HomeContract.View {

    private var banner: Banner? = null

    private lateinit var mBannerList: MutableList<BannerBean>

    private lateinit var mAdapter: HomeAdapter

    private var mPage: Int = 0


    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun createView(): HomeContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_common
    }

    override fun initView() {
        super.initView()

        val headerView = View.inflate(mContext, R.layout.layout_header_banner, null)
        banner = headerView.findViewById<View>(R.id.banner) as Banner?

        val manager = LinearLayoutManager(mContext)
        rvCommon.layoutManager = manager

        mAdapter = HomeAdapter(null)
        rvCommon.adapter = mAdapter

        mAdapter.addHeaderView(headerView)

        if (SettingManager.getListAnimationType() != 0) {
            mAdapter.openLoadAnimation(SettingManager.getListAnimationType())
        } else {
            mAdapter.closeLoadAnimation()
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

        titleBar.visibility = View.VISIBLE
        titleBar.setTitle("首页")
        titleBar.setLeftImage(0)
        titleBar.setRightImage(R.mipmap.icon_add)
        titleBar.setBgColor(SettingManager.getThemeColor())
        titleBar.setOnBarRightClickListener(object : OnBarRightClickListener{

            override fun rightClick(view: View?) {
                RouterManager.jump(RoutePath.URL_SEARCH)
            }
        })
    }

    override fun initSmartRefreshLayout() {
        super.initSmartRefreshLayout()

        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter()?.loadBannerData()
        getPresenter()?.loadListData(mPage)
    }

    override fun showBanner(result: MutableList<BannerBean>) {
        mBannerList = result

        val imgList = ArrayList<String>()
        val titleList = ArrayList<String>()
        for (bean in result) {
            imgList.add(bean.imagePath)
            titleList.add(bean.title)
        }
        banner?.setImages(imgList)
            ?.setImageLoader(GlideImageLoader())
            ?.setBannerAnimation(Transformer.Accordion)
            ?.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            ?.setBannerTitles(titleList)
            ?.setIndicatorGravity(BannerConfig.CENTER)
            ?.start()
    }

    override fun showList(result: MutableList<ArticleBean>) {
        if (mPage == 0) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
    }

    override fun addListeners() {
        banner?.setOnBannerListener { position ->
            val bean = mBannerList[position]
            RouterManager.jumpToWeb(bean.title, bean.url)
        }

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter()?.loadBannerData()
                getPresenter()?.loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter()?.loadListData(mPage)
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

    override fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.CHANGE_LIST_ANIMATION) {
            SettingManager.getListAnimationType().let {
                if (it != 0) {
                    mAdapter.openLoadAnimation(it)
                } else {
                    mAdapter.closeLoadAnimation()
                }
            }
        } else if (event?.code == EventCode.CHANGE_THEME) {
            SettingManager.getThemeColor().let {
                titleBar.setBgColor(it)

                ImmersionBar.with(this)
                    .statusBarColor(ColorUtils.int2RgbString(it))
                    .init()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        banner?.stopAutoPlay()
    }
}
