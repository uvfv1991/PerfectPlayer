package com.example.perfectplayer.ui.video

import com.aleyn.mvvm.base.BaseViewModel
import com.blankj.utilcode.util.TimeUtils
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.http.repository.AlbumRepository
import com.example.perfectplayer.utils.InjectorUtil
import java.text.SimpleDateFormat

class VideoDetailModel : BaseViewModel() {
    var repository: AlbumRepository? = null

    init {
        if (repository == null) {
            repository = InjectorUtil.getAlbumRepository()
        }
    }
    public fun getTime(): String {
        var format = SimpleDateFormat("HH:mm")
        return TimeUtils.getNowString(format)
    }

    fun saveHistory(video: Video) {
        repository?.videoDao?.saveVideoHistory(video)
    }
}
