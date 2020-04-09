package com.hjc.wan.ui.splash

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpTitleActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.splash.contract.SplashContract
import com.hjc.wan.ui.splash.presenter.SplashPresenter
import com.hjc.wan.utils.helper.RouterManager
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 14:03
 * @Description: 启动页
 */
class SplashActivity : BaseMvpTitleActivity<SplashContract.View, SplashPresenter>(),
    SplashContract.View {


    override fun createPresenter(): SplashPresenter {
        return SplashPresenter()
    }

    override fun createView(): SplashContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .navigationBarEnable(true)
            .init()

    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.visibility = View.GONE
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter()?.startCountdown()
    }

    override fun toLogin() {
        val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            ivLogo,
            getString(R.string.transition_logo_splash)
        )
        RouterManager.jumpWithScene(RoutePath.URL_LOGIN, this, compat)
        finish()
    }

    override fun toMain() {
        RouterManager.jump(RoutePath.URL_MAIN)
        finish()
    }

}