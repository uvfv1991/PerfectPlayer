package com.example.perfectplayer.ui.album

import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.PerfectApplication
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.manager.FileManager
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_LIST
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_NO
import java.io.File


class AlbumViewModel : BaseViewModel() {
    var dataList = ArrayList<Video>()
    var currentType = MutableLiveData<Int>()
    var fileManager =FileManager.getInstance()
    fun getAlbumList(foldername: String?) {
        getLocalVideo(foldername)

        currentType.value = TYPE_LIST
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getTitle() {
        val videos = fileManager.getVideoFolder()
        dataList= fileManager.useSetDistinct(videos!!)
        currentType.value = TYPE_NO
    }

    private fun getLocalVideo(foldername: String?):List<Video> {
        dataList.clear()
        fileManager.getVideos()
            ?.filter {it.folder.equals(foldername)}
            ?.forEach {  dataList.add(it)}

        return dataList
    }





}