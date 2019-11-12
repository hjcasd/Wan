package com.hjc.wan.http

import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.ui.model.LoginBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * @Author: HJC
 * @Date: 2019/1/7 11:53
 * @Description: Retrofit接口请求
 */
interface Api {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("username") username: String, @Field("password") pwd: String): Observable<BaseResponse<LoginBean>>
}
