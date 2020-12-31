package com.hjc.wan.ui.login

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.baselib.utils.helper.ActivityHelper
import com.hjc.baselib.widget.bar.OnBarLeftClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityRegisterBinding
import com.hjc.wan.ui.login.contract.RegisterContract
import com.hjc.wan.ui.login.presenter.RegisterPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager

/**
 * @Author: HJC
 * @Date: 2019/11/11 15:38
 * @Description: 注册页面
 */
@Route(path = RoutePath.URL_REGISTER)
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterContract.View, RegisterPresenter>(),
    RegisterContract.View {

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    override fun createView(): RegisterContract.View {
        return this
    }

    override fun initView() {
        super.initView()

        SettingManager.getThemeColor().let {
            mBinding.titleBar.setBgColor(it)
            mBinding.llRegister.setBackgroundColor(it)
        }
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun addListeners() {
        mBinding.btnRegister.setOnClickListener(this)

        mBinding.titleBar.setOnBarLeftClickListener(object : OnBarLeftClickListener {

            override fun leftClick(view: View) {
                finish()
            }
        })
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                preRegister()
            }
        }
    }

    /**
     * 注册准备
     */
    private fun preRegister() {
        val username = mBinding.etUsername.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()
        val confirmPassword = mBinding.etConfirmPassword.text.toString().trim()

        if (StringUtils.isEmpty(username)) {
            ToastUtils.showShort("请输入用户名")
            return
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码")
            return
        }
        if (StringUtils.isEmpty(confirmPassword)) {
            ToastUtils.showShort("请输入确认密码")
            return
        }

        if (username.length < 6) {
            ToastUtils.showShort("用户名长度至少为6位")
            return
        }
        if (password.length < 6) {
            ToastUtils.showShort("密码长度至少为6位")
            return
        }
        if (password.length < 6) {
            ToastUtils.showShort("确认密码长度至少为6位")
            return
        }
        if (password != confirmPassword) {
            ToastUtils.showShort("密码不一致")
            return
        }

        getPresenter().register(username, password)
    }


    override fun toLogin() {
        ActivityHelper.finishAllActivities()
        RouterManager.jump(RoutePath.URL_LOGIN)
    }

}