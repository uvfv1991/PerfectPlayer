package com.example.perfectplayer.base

import com.example.perfectplayer.data.db.VideoDao

object PerfectPlayerDatabase {

    private var videoDao: VideoDao? = null

    fun getVideoDao(): VideoDao {
        if (videoDao == null) {
            videoDao = VideoDao()
        }
        return videoDao!!
    }
}
