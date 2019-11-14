package com.hjc.wan.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.ui.contract.home.HomeContract
import com.hjc.wan.ui.fragment.home.adapter.HomeAdapter
import com.hjc.wan.ui.model.ArticleBean
import com.hjc.wan.ui.model.BannerBean
import com.hjc.wan.ui.presenter.home.HomePresenter
import com.hjc.wan.utils.image.GlideImageLoader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:35
 * @Description: 首页
 */
class HomeFragment : BaseMvpFragment<HomeContract.View, HomePresenter>(), HomeContract.View {

    private var banner: Banner? = null

    private lateinit var mHomeAdapter: HomeAdapter

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
        return R.layout.fragment_home
    }

    override fun initView() {
        super.initView()

        val headerView = View.inflate(mContext, R.layout.layout_header_banner, null)
        banner = headerView.findViewById<View>(R.id.banner) as Banner?

        val manager = LinearLayoutManager(mContext)
        rvHome.layoutManager = manager

        mHomeAdapter = HomeAdapter(null)
        rvHome.adapter = mHomeAdapter

        mHomeAdapter.addHeaderView(headerView)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        showLoading()
        getPresenter().loadBannerData()
        getPresenter().loadListData(mPage)
    }

    override fun showBanner(result: MutableList<BannerBean>) {
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
            mHomeAdapter.setNewData(result)
        } else {
            mHomeAdapter.addData(result)
        }
    }

    override fun addListeners() {
        banner?.setOnBannerListener { position -> ToastUtils.showShort("position: $position") }

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 0
                getPresenter().loadBannerData()
                getPresenter().loadListData(mPage)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getPresenter().loadListData(mPage)
            }

        })

        mHomeAdapter.setOnCollectViewClickListener(object : HomeAdapter.OnCollectViewClickListener{

            override fun onClick(checkBox: CheckBox, position: Int) {
                if (checkBox.isChecked){
                    ToastUtils.showShort("选中了: $position")
                }else{
                    ToastUtils.showShort("未选中: $position")
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                stateView.showContent()
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.setEnableLoadMore(true)
            }
    }

    override fun showLoading() {
        super.showLoading()
        stateView.showLoading()
    }

    override fun showError() {
        stateView.showError()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showEmpty() {
        stateView.showEmpty()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showNoNetwork() {
        stateView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun onDestroyView() {
        banner?.stopAutoPlay()
        super.onDestroyView()
    }

}
