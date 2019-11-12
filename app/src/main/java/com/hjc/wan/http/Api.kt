package com.hjc.wan.http

import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.ui.model.BannerBean
import com.hjc.wan.ui.model.HomeArticleBean
import com.hjc.wan.ui.model.LoginBean
import com.hjc.wan.http.bean.BasePageResponse
import io.reactivex.Observable
import retrofit2.http.*


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


    /**
     * 获取banner数据
     */
    @GET("/banner/json")
    fun getBanner(): Observable<BaseResponse<MutableList<BannerBean>>>


    /**
     * 玩安卓，文章列表、知识体系下的文章列表
     *
     * @param page 页码，从0开始
     * @param cid  体系id
     */
    @GET("article/list/{page}/json")
     fun getArticle(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<HomeArticleBean>>>>
}
