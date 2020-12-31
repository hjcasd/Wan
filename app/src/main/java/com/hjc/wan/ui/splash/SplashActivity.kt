package com.hjc.wan.ui.splash

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivitySplashBinding
import com.hjc.wan.ui.splash.contract.SplashContract
import com.hjc.wan.ui.splash.presenter.SplashPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager

/**
 * @Author: HJC
 * @Date: 2019/11/11 14:03
 * @Description: 启动页
 */
class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashContract.View, SplashPresenter>(),
    SplashContract.View {


    override fun createPresenter(): SplashPresenter {
        return SplashPresenter()
    }

    override fun createView(): SplashContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .fullScreen(true)
            .navigationBarEnable(true)
    }

    override fun initView() {
        super.initView()

        mBinding.rlSplash.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        getPresenter().startCountdown()
    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    override fun toLogin() {
        val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            mBinding.ivLogo,
            getString(R.string.app_transition_logo_splash)
        )
        RouterManager.jumpWithScene(RoutePath.URL_LOGIN, this, compat)
        finish()
    }

    override fun toMain() {
        RouterManager.jump(RoutePath.URL_MAIN)
        finish()
    }

}