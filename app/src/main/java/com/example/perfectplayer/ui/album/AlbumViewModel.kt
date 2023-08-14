package com.example.perfectplayer.ui.album

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.http.repository.AlbumRepository
import com.example.perfectplayer.manager.FileManager
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_History
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_LIST
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_NO
import com.example.perfectplayer.utils.InjectorUtil

class AlbumViewModel : BaseViewModel() {
    var dataList = ArrayList<Video>()
    var currentType = MutableLiveData<Int>()
    var fileManager = FileManager.getInstance()
    var mfoldername: String? = null
    var repository: AlbumRepository? = null

    init {
        if (repository == null) {
            repository = InjectorUtil.getAlbumRepository()
        }
    }
    fun getAlbumList(foldername: String?) {
        mfoldername = foldername
        getLocalVideo(foldername)

        currentType.value = TYPE_LIST
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTitle() {
        dataList.clear()
        val videos = fileManager.getVideoFolder()
        dataList = fileManager.useSetDistinct(videos!!)
        currentType.value = TYPE_NO
    }

    private fun getLocalVideo(foldername: String?): List<Video> {
        dataList.clear()
        fileManager.getVideos()
            ?.filter { it.folder.equals(foldername) }
            ?.forEach { dataList.add(it) }

        return dataList
    }

    fun getHistory(){
        dataList.clear()
        dataList = repository?.getVideoList() as ArrayList<Video>
        currentType.value = TYPE_History
    }
}
