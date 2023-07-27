package com.example.perfectplayer.http

import com.aleyn.mvvm.base.BaseResponse
import com.example.perfectplayer.data.Album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface AlbumService {


    /**
     * 获取首页文章列表数据
     *
     * @param pageNum 页数
     * @return 文章列表数据
     */
    @GET("article/list/{pageNum}/json")
    suspend fun  getArticleListData(@Path("pageNum") pageNum: Int): BaseResponse<Album>

    /**
     * 获取首页Banner数据
     *
     * @return Banner数据
     */
    @GET("banner/json")
    suspend fun  getBannerData(): BaseResponse<ArrayList<Album>>

    @GET()
    fun getCommentData(@Url path:String):Call<Any>
}