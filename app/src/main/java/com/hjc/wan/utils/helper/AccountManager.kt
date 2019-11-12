package com.hjc.wan.utils.helper

import com.blankj.utilcode.util.CacheDiskUtils
import com.blankj.utilcode.util.SPUtils
import com.hjc.wan.ui.model.LoginBean

/**
 * @Author: HJC
 * @Date: 2019/2/20 15:39
 * @Description: 账户管理类
 */
object AccountManager {

    private const val KEY_IS_LOGIN = "isLogin"
    private const val KEY_LOGIN_INFO = "loginInfo"
    private const val KEY_COOKIE = "cookie"

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    fun getLoginInfo(): LoginBean {
        return CacheDiskUtils.getInstance().getSerializable(KEY_LOGIN_INFO) as LoginBean
    }

    /**
     * 保存用户信息
     * @param bean 用户信息
     */
    fun setLoginInfo(bean: LoginBean) {
        CacheDiskUtils.getInstance().put(KEY_LOGIN_INFO, bean)
    }

    /**
     * 获取用户是否登录
     *
     * @return
     */
    fun isLogin(): Boolean {
        return SPUtils.getInstance().getBoolean(KEY_IS_LOGIN)
    }

    fun setLogin(isLogin: Boolean) {
        SPUtils.getInstance().put(KEY_IS_LOGIN, isLogin)
    }

    /**
     * 清除账户信息
     */
    fun clear() {
        SPUtils.getInstance().clear()
        CacheDiskUtils.getInstance().clear()
    }
}
