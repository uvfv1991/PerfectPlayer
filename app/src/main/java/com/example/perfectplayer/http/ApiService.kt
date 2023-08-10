package com.example.perfectplayer.http

import android.database.Observable
import android.provider.MediaStore.Audio.Albums
import com.aleyn.mvvm.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Url

/**
 *  author : jiangxue
 *  date : 2023/6/1 16:49
 *  description :网络的api
 */

open interface ApiService {
    /* -----------HomeData--------------*/
    /**
     * 获取首页Banner数据
     *
     * @return Banner数据
     */
    @GET("banner/json")
    fun getBannerData(): Observable<BaseResponse<List<Albums>>>

    /**
     * 获取导航列表数据
     *
     * @return 导航列表数据
     */
    @GET
    fun getDiscoverData(@Url path: String): Observable<Any>
}
