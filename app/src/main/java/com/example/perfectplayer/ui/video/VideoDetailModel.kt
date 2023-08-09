package com.example.perfectplayer.ui.video

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.manager.FileManager


class VideoDetailModel : BaseViewModel() {
    var dataList = ArrayList<Video>()
    var currentType = MutableLiveData<Int>()
    var fileManager =FileManager.getInstance()






}