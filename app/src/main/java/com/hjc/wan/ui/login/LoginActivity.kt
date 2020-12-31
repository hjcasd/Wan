package com.hjc.wan.ui.login

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityLoginBinding
import com.hjc.wan.ui.login.contract.LoginContract
import com.hjc.wan.ui.login.presenter.LoginPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager

/**
 * @Author: HJC
 * @Date: 2019/11/11 15:38
 * @Description: 登录页面
 */
@Route(path = RoutePath.URL_LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginContract.View, LoginPresenter>(),
    LoginContract.View {

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun createView(): LoginContract.View {
        return this
    }

    override fun initView() {
        super.initView()

        mBinding.loginLayout.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .keyboardEnable(true)
            .setOnKeyboardListener { isPopup, _ ->
                if (isPopup) {
                    mBinding.loginLayout.setKeyBoardShow(mBinding.llBg, mBinding.rlTop, true)
                } else {
                    mBinding.loginLayout.setKeyBoardShow(mBinding.llBg, mBinding.rlTop, false)
                }
            }
            .fitsSystemWindows(true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun addListeners() {
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.tvRegister.setOnClickListener(this)
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                preLogin()
            }
            R.id.tv_register -> {
                RouterManager.jump(RoutePath.URL_REGISTER)
            }
        }
    }

    /**
     * 登录准备
     */
    private fun preLogin() {
        val username = mBinding.etUsername.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()

        if (StringUtils.isEmpty(username)) {
            ToastUtils.showShort("请输入账号")
            return
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码")
            return
        }

        if (username.length < 6) {
            ToastUtils.showShort("账号长度至少为6位")
            return
        }
        if (password.length < 6) {
            ToastUtils.showShort("密码长度至少为6位")
            return
        }

        getPresenter().login(username, password)
    }

    override fun toMain() {
        RouterManager.jump(RoutePath.URL_MAIN)
        finish()
    }

    override fun toRegister() {
        RouterManager.jump(RoutePath.URL_REGISTER)
    }

}