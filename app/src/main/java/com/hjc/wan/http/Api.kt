package com.hjc.wan.http

import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.model.*
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
     * 获取首页列表数据
     */
    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取项目分类数据
     */
    @GET("/project/tree/json")
    fun getProjectTypes(): Observable<BaseResponse<MutableList<ClassifyBean>>>


    /**
     * 根据分类id获取项目数据
     */
    @GET("/project/list/{page}/json")
    fun getProjectDataByType(@Path("page") pageNo: Int, @Query("cid") cid: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取广场数据
     */
    @GET("/user_article/list/{page}/json")
    fun getSquareData(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    fun getSystemData(): Observable<BaseResponse<MutableList<SystemBean>>>


    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    fun getNavigationData(): Observable<BaseResponse<MutableList<NavigationBean>>>
}
