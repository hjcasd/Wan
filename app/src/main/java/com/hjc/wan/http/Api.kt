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
     * 注册
     */
    @FormUrlEncoded
    @POST("/user/register")
    fun register(@Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String): Observable<BaseResponse<Any>>

    /**
     * 退出
     */
    @GET("user/logout/json")
    fun logout(): Observable<BaseResponse<Any>>


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
    fun getSquare(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    fun getSystem(): Observable<BaseResponse<MutableList<SystemBean>>>


    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    fun getNavigation(): Observable<BaseResponse<MutableList<NavigationBean>>>


    /**
     * 知识体系下的文章数据
     */
    @GET("/article/list/{page}/json")
    fun getSystemTag(@Path("page") pageNo: Int, @Query("cid") cid: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取公众号分类数据
     */
    @GET("/wxarticle/chapters/json")
    fun getPublicTypes(): Observable<BaseResponse<MutableList<ClassifyBean>>>


    /**
     * 根据分类id获取公众号数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    fun getPublicDataByType(@Path("page") pageNo: Int, @Path("id") id: Int): Observable<BaseResponse<BasePageResponse<MutableList<ArticleBean>>>>


    /**
     * 获取当前账户的个人积分
     */
    @GET("/lg/coin/userinfo/json")
    fun getIntegral(): Observable<BaseResponse<IntegralBean>>


    /**
     * 列表中收藏文章
     */
    @POST("/lg/collect/{id}/json")
    fun collect(@Path("id") id: Int): Observable<BaseResponse<Any>>

    /**
     * 列表中取消收藏文章
     */
    @POST("/lg/uncollect_originId/{id}/json")
    fun unCollect(@Path("id") id: Int): Observable<BaseResponse<Any>>

    /**
     * 详情页面收藏网址
     */
    @POST("/lg/collect/addtool/json")
    fun collectLink(@Query("name") name: String, @Query("link") link: String): Observable<BaseResponse<CollectLinkBean>>


    /**
     * 获取积分排行榜数据
     */
    @GET("/coin/rank/{page}/json")
    fun getIntegralRank(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<IntegralBean>>>>

    /**
     * 获取积分历史数据
     */
    @GET("/lg/coin/list/{page}/json")
    fun getIntegralHistory(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<IntegralHistoryBean>>>>


    /**
     * 获取收藏的文章列表数据
     */
    @GET("/lg/collect/list/{page}/json")
    fun getCollectArticle(@Path("page") pageNo: Int): Observable<BaseResponse<BasePageResponse<MutableList<CollectArticleBean>>>>

    /**
     * 取消收藏的文章
     */
    @POST("/lg/uncollect/{id}/json")
    fun unCollectOrigin(@Path("id") id: Int, @Query("originId") originId: Int): Observable<BaseResponse<Any>>


    /**
     * 获取收藏的网址列表数据
     */
    @GET("/lg/collect/usertools/json")
    fun getCollectLink(): Observable<BaseResponse<MutableList<CollectLinkBean>>>

    /**
     * 取消收藏的网址
     */
    @POST("/lg/collect/deletetool/json")
    fun deleteLink(@Query("id") id: Int): Observable<BaseResponse<Any>>


    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    fun getTodoData(@Path("page") page: Int): Observable<BaseResponse<BasePageResponse<MutableList<TodoBean>>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): Observable<BaseResponse<Any>>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): Observable<BaseResponse<Any>>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") id: Int): Observable<BaseResponse<Any>>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): Observable<BaseResponse<Any>>
}
