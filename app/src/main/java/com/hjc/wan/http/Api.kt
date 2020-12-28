package com.hjc.wan.http

import com.hjc.wan.http.bean.BaseResponse
import com.hjc.wan.http.bean.PageResponse
import com.hjc.wan.model.*
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
    suspend fun login(@Field("username") username: String, @Field("password") pwd: String):BaseResponse<LoginBean>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(@Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String): BaseResponse<Any>

    /**
     * 退出
     */
    @GET("user/logout/json")
    suspend fun logout(): BaseResponse<Any>


    /**
     * 获取banner数据
     */
    @GET("/banner/json")
    suspend fun getBanner(): BaseResponse<MutableList<BannerBean>>


    /**
     * 获取首页列表数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page") page: Int): BaseResponse<PageResponse<MutableList<ArticleBean>>>


    /**
     * 获取项目分类数据
     */
    @GET("/project/tree/json")
    suspend fun getProjectTypes(): BaseResponse<MutableList<ClassifyBean>>


    /**
     * 根据分类id获取项目数据
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectDataByType(@Path("page") pageNo: Int, @Query("cid") cid: Int): BaseResponse<PageResponse<MutableList<ArticleBean>>>


    /**
     * 获取广场数据
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getSquare(@Path("page") page: Int): BaseResponse<PageResponse<MutableList<ArticleBean>>>


    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    suspend fun getSystem(): BaseResponse<MutableList<SystemBean>>


    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    suspend fun getNavigation():BaseResponse<MutableList<NavigationBean>>


    /**
     * 知识体系下的文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getSystemTag(@Path("page") pageNo: Int, @Query("cid") cid: Int): BaseResponse<PageResponse<MutableList<ArticleBean>>>


    /**
     * 获取公众号分类数据
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getPublicTypes(): BaseResponse<MutableList<ClassifyBean>>


    /**
     * 根据分类id获取公众号数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getPublicDataByType(@Path("page") pageNo: Int, @Path("id") id: Int): BaseResponse<PageResponse<MutableList<ArticleBean>>>


    /**
     * 获取当前账户的个人积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getIntegral(): BaseResponse<IntegralBean>


    /**
     * 列表中收藏文章
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 列表中取消收藏文章
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 详情页面收藏网址
     */
    @POST("/lg/collect/addtool/json")
    suspend fun collectLink(@Query("name") name: String, @Query("link") link: String): BaseResponse<CollectLinkBean>


    /**
     * 获取积分排行榜数据
     */
    @GET("/coin/rank/{page}/json")
    suspend fun getIntegralRank(@Path("page") page: Int): BaseResponse<PageResponse<MutableList<IntegralBean>>>

    /**
     * 获取积分历史数据
     */
    @GET("/lg/coin/list/{page}/json")
    suspend fun getIntegralHistory(@Path("page") page: Int): BaseResponse<PageResponse<MutableList<IntegralHistoryBean>>>


    /**
     * 获取收藏的文章列表数据
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollectArticle(@Path("page") pageNo: Int): BaseResponse<PageResponse<MutableList<CollectArticleBean>>>

    /**
     * 取消收藏的文章
     */
    @POST("/lg/uncollect/{id}/json")
    suspend fun unCollectOrigin(@Path("id") id: Int, @Query("originId") originId: Int): BaseResponse<Any>


    /**
     * 获取收藏的网址列表数据
     */
    @GET("/lg/collect/usertools/json")
    suspend fun getCollectLink(): BaseResponse<MutableList<CollectLinkBean>>

    /**
     * 取消收藏的网址
     */
    @POST("/lg/collect/deletetool/json")
    suspend fun deleteLink(@Query("id") id: Int): BaseResponse<Any>


    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun getTodoData(@Path("page") page: Int): BaseResponse<PageResponse<MutableList<TodoBean>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): BaseResponse<Any>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): BaseResponse<Any>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): BaseResponse<Any>


    /**
     * 获取热门搜索数据
     */
    @GET("/hotkey/json")
    suspend fun getHotKey(): BaseResponse<MutableList<SearchBean>>

    /**
     * 根据关键词搜索数据
     */
    @POST("/article/query/{page}/json")
    suspend fun search(@Path("page") pageNo: Int, @Query("k") searchKey: String):BaseResponse<PageResponse<MutableList<ArticleBean>>>
}
