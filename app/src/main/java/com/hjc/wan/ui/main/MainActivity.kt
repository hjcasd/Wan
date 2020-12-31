package com.hjc.wan.ui.main

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.baselib.activity.BaseFragmentActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityMainBinding
import com.hjc.wan.ui.home.HomeFragment
import com.hjc.wan.ui.main.contract.MainContract
import com.hjc.wan.ui.main.presenter.MainPresenter
import com.hjc.wan.ui.mine.MineFragment
import com.hjc.wan.ui.project.ProjectFragment
import com.hjc.wan.ui.publics.PublicFragment
import com.hjc.wan.ui.square.SquareFragment

/**
 * @Author: HJC
 * @Date: 2019/11/11 14:03
 * @Description: 主界面
 */
@Route(path = RoutePath.URL_MAIN)
class MainActivity : BaseFragmentActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mProjectFragment: ProjectFragment
    private lateinit var mSquareFragment: SquareFragment
    private lateinit var mPublicFragment: PublicFragment
    private lateinit var mMineFragment: MineFragment


    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun createView(): MainContract.View {
        return this
    }

    override fun getFragmentContentId(): Int {
        return R.id.fl_content
    }

    override fun initData(savedInstanceState: Bundle?) {
        getPresenter()?.requestPermission()

        mHomeFragment = HomeFragment.newInstance()
        mProjectFragment = ProjectFragment.newInstance()
        mSquareFragment = SquareFragment.newInstance()
        mPublicFragment = PublicFragment.newInstance()
        mMineFragment = MineFragment.newInstance()

        showFragment(mHomeFragment)
    }

    override fun addListeners() {
        mBinding.rgTab.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_home -> showFragment(mHomeFragment)

                R.id.rb_project -> showFragment(mProjectFragment)

                R.id.rb_square -> showFragment(mSquareFragment)

                R.id.rb_public -> showFragment(mPublicFragment)

                R.id.rb_mine -> showFragment(mMineFragment)
            }
        }
    }

    override fun onSingleClick(v: View?) {

    }
}
