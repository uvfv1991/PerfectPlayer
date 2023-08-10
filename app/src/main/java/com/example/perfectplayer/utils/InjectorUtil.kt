package com.example.perfectplayer.utils

import com.example.perfectplayer.http.AlbumNetWork
import com.example.perfectplayer.http.repository.AlbumRepository

/**
 *  author : jiangxue
 *  date : 2023/5/31 16:53
 *  description :仓库注入工具
 */
object InjectorUtil {
    // 相册视频
    fun getAlbumRepository() = AlbumRepository.getInstance(AlbumNetWork.getInstance())
}
