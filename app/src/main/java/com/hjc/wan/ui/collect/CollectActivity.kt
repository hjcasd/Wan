package com.hjc.wan.ui.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.collect.adapter.CollectAdapter
import com.hjc.wan.ui.collect.child.CollectArticleFragment
import com.hjc.wan.ui.collect.child.CollectLinkFragment
import com.hjc.wan.ui.collect.contract.CollectContract
import com.hjc.wan.ui.collect.presenter.CollectPresenter
import com.hjc.wan.utils.helper.SettingManager
import kotlinx.android.synthetic.main.activity_collect.*
import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:23
 * @Description: 我的收藏页面
 */
@Route(path = RoutePath.URL_COLLECT)
class CollectActivity : BaseMvpActivity<CollectContract.View, CollectPresenter>(),
    CollectContract.View {


    override fun createPresenter(): CollectPresenter {
        return CollectPresenter()
    }

    override fun createView(): CollectContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_collect
    }


    override fun initView() {
        super.initView()

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        colToolbar.run {
            setContentScrimColor(SettingManager.getThemeColor())
            setStatusBarScrimColor(SettingManager.getThemeColor())
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .titleBar(toolbar)
            .fitsSystemWindows(true)
            .init()
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter()?.loadChild()
    }

    override fun addListeners() {
        super.addListeners()

        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun showFragment() {
        val titles = mutableListOf("文章", "网址")

        val fragments = ArrayList<Fragment>()

        val collectArticleFragment = CollectArticleFragment.newInstance()
        val collectLinkFragment = CollectLinkFragment.newInstance()

        fragments.add(collectArticleFragment)
        fragments.add(collectLinkFragment)

        val adapter = CollectAdapter(
            supportFragmentManager,
            fragments,
            titles
        )
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
    }

}