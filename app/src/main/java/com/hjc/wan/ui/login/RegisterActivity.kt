package com.hjc.wan.ui.login

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.activity.BaseMvpTitleActivity
import com.hjc.baselib.utils.helper.ActivityManager
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.login.contract.RegisterContract
import com.hjc.wan.ui.login.presenter.RegisterPresenter
import com.hjc.wan.utils.helper.RouterManager
import com.hjc.wan.utils.helper.SettingManager
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.etUsername
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 15:38
 * @Description: 注册页面
 */
@Route(path = RoutePath.URL_REGISTER)
class RegisterActivity : BaseMvpTitleActivity<RegisterContract.View, RegisterPresenter>(),
    RegisterContract.View {

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    override fun createView(): RegisterContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.setTitle("账号注册")
        titleBar.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun addListeners() {
        super.addListeners()

        btnRegister.setOnClickListener(this)
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)

        when (v?.id) {
            R.id.btnRegister -> {
                preRegister()
            }
        }
    }

    /**
     * 注册准备
     */
    private fun preRegister() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

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

        getPresenter()?.register(username, password)
    }


    override fun toLogin() {
        ActivityManager.finishAllActivities()
        RouterManager.jump(RoutePath.URL_LOGIN)
    }

}