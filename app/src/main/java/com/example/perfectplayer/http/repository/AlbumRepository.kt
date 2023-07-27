package com.example.perfectplayer.http.repository

import android.util.Log
import com.aleyn.mvvm.base.BaseModel
import com.example.perfectplayer.http.AlbumNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  author : jiangxue
 *  date : 2023/6/1 15:59
 *  description :数据仓库(可以扩展本地和网络的数据)
 */

class AlbumRepository private constructor(private val network: AlbumNetWork): BaseModel() {
    fun getAlbumList(): Any {

        return TODO("Provide the return value")
    }



    //一个类里面只能声明一个内部关联对象，即关键字 companion 只能使用一次。直接通过外部类访问到对象的内部元素
    companion object {

        private lateinit var instance:AlbumRepository

        fun getInstance(network: AlbumNetWork): AlbumRepository {
            if (!::instance.isInitialized) {
                synchronized(AlbumRepository::class.java) {
                    if (!::instance.isInitialized) {
                        instance = AlbumRepository(network)
                    }
                }
            }
            return instance
        }

    }

}