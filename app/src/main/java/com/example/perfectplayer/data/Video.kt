package com.example.perfectplayer.data

import android.graphics.Bitmap
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  author : jiangxue
 *  date : 2023/7/28 9:58
 *  description :
 */


class Video(@SerializedName("path") var path: String?,
            @SerializedName("duration") var duration: Int?,
            @SerializedName("size") var size: Long?,
            @SerializedName("thumbPath") var thumbPath: String?,
            @SerializedName("bitmap") var bitmap: Bitmap?,
            @SerializedName("videoicon") var videoicon: String?,
            @SerializedName("name") var videoName: String?,
            @SerializedName("count") var count: Int,
            @SerializedName("itemtype") var type: Int
): MultiItemEntity, Serializable {

    companion object {
        const val title = 0
        const val list = 1
    }
    override val itemType: Int
        get() = type

}

