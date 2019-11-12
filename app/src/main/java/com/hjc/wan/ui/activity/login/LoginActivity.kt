package com.hjc.wan.ui.activity.login

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.contract.login.LoginContract
import com.hjc.wan.ui.presenter.login.LoginPresenter
import com.hjc.wan.utils.helper.RouterManager
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 15:38
 * @Description: 登录页面
 */
@Route(path = RoutePath.URL_LOGIN)
class LoginActivity : BaseMvpActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun createView(): LoginContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun addListeners() {
        btnLogin.setOnClickListener(this)
        tvRegister.setOnClickListener(this)
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                preLogin()
            }
            R.id.tvRegister -> {
                ToastUtils.showShort("注册")
            }
        }
    }

    /**
     * 登录准备
     */
    private fun preLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (StringUtils.isEmpty(username)) {
            ToastUtils.showShort("请输入用户名")
            return
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码")
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

        getPresenter().login(username, password)
    }

    override fun toMain() {
        RouterManager.jump(RoutePath.URL_MAIN)
    }

    override fun toRegister() {
        RouterManager.jump(RoutePath.URL_REGISTER)
    }

}