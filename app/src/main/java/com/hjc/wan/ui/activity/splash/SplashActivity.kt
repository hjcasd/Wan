package com.hjc.wan.ui.activity.splash

import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.contract.splash.SplashContract
import com.hjc.wan.ui.presenter.splash.SplashPresenter
import com.hjc.wan.utils.helper.RouterManager
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 14:03
 * @Description: 启动页
 */
class SplashActivity : BaseMvpActivity<SplashContract.View, SplashPresenter>(),
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

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter().startCountdown()
    }

    override fun toLogin() {
        val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            ivLogo,
            getString(R.string.transition_logo_splash)
        )
        RouterManager.jumpWithScene(RoutePath.URL_LOGIN, this, compat)
    }

}