package com.example.perfectplayer.ui.album

import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.PerfectApplication
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_LIST
import com.example.perfectplayer.ui.album.AlbumFragment.Companion.TYPE_NO
import java.io.File


class AlbumViewModel : BaseViewModel() {
    val dataList = ArrayList<Video>()
    var currentType = MutableLiveData<Int>()
    var dataChange = MutableLiveData<Int>()
    fun getAlbumList() {
        //获取本地的视频

        getLocalVideo()
        currentType.value = TYPE_LIST

    }
    fun getTitle() {

        val album = Video(null,null,null,null,null,null, "相册视频", getVideoList(),TYPE_NO)
        dataList.add(album)
        currentType.value = TYPE_NO
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }


    private fun getVideoList():Int {
        dataList.clear()
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

    private fun getLocalVideo():List<Video> {
        dataList.clear()
        val sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC "
        val cursor: Cursor = PerfectApplication.context.getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOrder
        )


        if (null != cursor && cursor.moveToFirst()) {
            do {
                val info = Video(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)),
                    null,
                    null,
                    File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)).toString()).name,
                    0,
                    TYPE_LIST
                )
                dataList.add(info);
            } while (cursor.moveToNext());
               cursor.close();
        }


        return dataList
    }





}