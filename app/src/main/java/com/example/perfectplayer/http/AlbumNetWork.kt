package com.example.perfectplayer.http

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *  author : jiangxue
 *  date : 2023/6/1 16:41
 *  description :
 */

class AlbumNetWork {

    private val service = ServiceCreator.create(AlbumService::class.java)

    //首页数据  await()在kotlin中是一个挂起函数，他会等待结果返回才继续执行之后的代
    suspend fun requestArticle(pageNum: Int) = service.getArticleListData(pageNum)

    suspend fun requestComment(url: String) = service.getCommentData(url).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }


    companion object {
        @Volatile
        private var netWork: AlbumNetWork? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: AlbumNetWork().also { netWork = it }
        }
    }

}



