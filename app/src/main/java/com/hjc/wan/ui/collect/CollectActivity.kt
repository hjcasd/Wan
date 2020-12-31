package com.hjc.wan.ui.collect

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityCollectBinding
import com.hjc.wan.ui.collect.adapter.CollectAdapter
import com.hjc.wan.ui.collect.child.CollectArticleFragment
import com.hjc.wan.ui.collect.child.CollectLinkFragment
import com.hjc.wan.ui.collect.contract.CollectContract
import com.hjc.wan.ui.collect.presenter.CollectPresenter
import com.hjc.wan.utils.helper.SettingManager
import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/12/23 15:23
 * @Description: 我的收藏页面
 */
@Route(path = RoutePath.URL_COLLECT)
class CollectActivity :
    BaseActivity<ActivityCollectBinding, CollectContract.View, CollectPresenter>(),
    CollectContract.View {


    override fun createPresenter(): CollectPresenter {
        return CollectPresenter()
    }

    override fun createView(): CollectContract.View {
        return this
    }


    override fun initView() {
        super.initView()

        setSupportActionBar(mBinding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        SettingManager.getThemeColor().let { color ->
            mBinding.toolbarLayout.run {
                setContentScrimColor(color)
                setContentScrimColor(color)
            }
        }
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .titleBar(mBinding.toolbar)
            .fitsSystemWindows(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        getPresenter().loadChild()
    }

    override fun addListeners() {
        mBinding.toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onSingleClick(v: View?) {

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
        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.offscreenPageLimit = 3
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

}