package com.hjc.wan.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpFragmentActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.contract.MainContract
import com.hjc.wan.ui.fragment.MineFragment
import com.hjc.wan.ui.fragment.PublicFragment
import com.hjc.wan.ui.fragment.home.HomeFragment
import com.hjc.wan.ui.fragment.project.ProjectFragment
import com.hjc.wan.ui.fragment.square.SquareFragment
import com.hjc.wan.ui.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 14:03
 * @Description: 主界面
 */
@Route(path = RoutePath.URL_MAIN)
class MainActivity : BaseMvpFragmentActivity<MainContract.View, MainPresenter>(),
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

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getFragmentContentId(): Int {
        return R.id.flContent
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter().requestPermission()

        mHomeFragment = HomeFragment.newInstance()
        mProjectFragment = ProjectFragment.newInstance()
        mSquareFragment = SquareFragment.newInstance()
        mPublicFragment = PublicFragment.newInstance()
        mMineFragment = MineFragment.newInstance()

        showFragment(mHomeFragment)
    }

    override fun addListeners() {
        super.addListeners()

        rgTab.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbHome -> showFragment(mHomeFragment)

                R.id.rbProject -> showFragment(mProjectFragment)

                R.id.rbSquare -> showFragment(mSquareFragment)

                R.id.rbPublic -> showFragment(mPublicFragment)

                R.id.rbMine -> showFragment(mMineFragment)
            }
        }
    }
}
