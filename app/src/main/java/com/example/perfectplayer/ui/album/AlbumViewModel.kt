package com.example.perfectplayer.ui.album

import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.PerfectApplication
import com.example.perfectplayer.data.Album
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_LIST
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_NO
import com.example.perfectplayer.utils.InjectorUtil
import java.io.File


class AlbumViewModel : BaseViewModel() {
    val dataList = ArrayList<Album>()
    var currentType = MutableLiveData<Int>()
    fun getAlbumList() {
        //获取本地的视频
        currentType.value = TYPE_LIST


    }
    fun getTitle() {
        currentType.value = TYPE_NO
        val album = Album(null, "相册视频", getVideoList().toString())
        dataList.add(album)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }


    private val repository by lazy { InjectorUtil.getAlbumRepository() }


    private fun getVideoList():Int {
        var count=0
        val sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC "
        val cursor: Cursor = PerfectApplication.context.getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOrder
        )
        if (null != cursor && cursor.moveToFirst()) {
            count = cursor.count
            Log.d(this.toString(), "共有视频: $count")
            cursor.close()
        }
        return count
    }



}