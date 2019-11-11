package com.hjc.wan.utils.helper

import com.blankj.utilcode.util.SPUtils

/**
 * @Author: HJC
 * @Date: 2019/2/20 15:39
 * @Description: 账户管理类
 */
object AccountManager {

    private const val KEY_IS_LOGIN = "isLogin"
    private const val KEY_USERNAME = "username"
    private const val KEY_COOKIE = "cookie"

    /**
     * 获取用户名
     *
     * @return
     */
    fun getUsername(): String {
        return SPUtils.getInstance().getString(KEY_USERNAME)
    }

    fun setUsername(username: String) {
        SPUtils.getInstance().put(KEY_USERNAME, username)
    }

    /**
     * 获取cookie
     */
    fun getCookie(): String {
        return SPUtils.getInstance().getString(KEY_COOKIE)
    }

    fun setCookie(cookie: String) {
        SPUtils.getInstance().put(KEY_COOKIE, cookie)
    }

    /**
     * 用户是否登录
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
        setLogin(false)
        setUsername("")
        setCookie("")
    }
}
