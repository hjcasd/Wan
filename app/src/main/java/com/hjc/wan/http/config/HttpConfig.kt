package com.hjc.wan.http.config

import com.hjc.wan.BuildConfig

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:40
 * @Description:  网络基本配置
 */
object HttpConfig {

    //超时时间(s)
    const val HTTP_TIME_OUT = 20

    //服务器地址
    const val BASE_URL = BuildConfig.BASE_URL
}
