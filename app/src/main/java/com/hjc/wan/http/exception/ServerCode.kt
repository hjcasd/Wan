package com.hjc.wan.http.exception

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:48
 * @Description: Code码说明类
 */
object ServerCode {
    const val CODE_SUCCESS = "000"  //请求成功
    const val CODE_ID_ERROR = "003"  //身份证过期
    const val CODE_VERIFY_SMS = "005"  //需要短信验证码验证
    const val CODE_TOKEN_ERROR = "104"  //登录状态失效
}
