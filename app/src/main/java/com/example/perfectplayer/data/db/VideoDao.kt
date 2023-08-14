package com.example.perfectplayer.data.db

import android.util.Log
import com.example.perfectplayer.data.Video
import org.litepal.LitePal
/**
 *  author : jiangxue
 *  date : 2023/8/14 9:15
 *  description :
 */
class VideoDao {

    fun getVideoHistoryList(): List<Video> = LitePal.findAll(Video::class.java)

    fun saveVideoHistoryList(videoList: List<Video>?) {
        if (videoList != null && videoList.isNotEmpty()) {
            LitePal.saveAll(videoList)
        }
    }

    fun saveVideoHistory(video: Video) {
        val save = video.save()
        Log.i("saveVideoHistory", " 插入成功")
    }
}
