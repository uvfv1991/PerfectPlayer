package com.example.perfectplayer.data

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  author : jiangxue
 *  date : 2023/7/28 9:58
 *  description :
 */

class Video(
    @SerializedName("path") var path: String?,
    @SerializedName("isshow") var isshow: Boolean,
    @SerializedName("size") var size: Long?,
    @SerializedName("thumbPath") var thumbPath: String?,
    @SerializedName("ischeck") var ischeck: Boolean,
    @SerializedName("folder") var folder: String?,
    @SerializedName("name") var videoName: String?,
    @SerializedName("count") var count: Int,
    @SerializedName("itemtype") var type: Int,
) : MultiItemEntity, Serializable {
    companion object {
        const val title = 0
        const val list = 1
        const val history = 2
    }
    override val itemType: Int
        get() = type

    override fun equals(other: Any?): Boolean {
        var v = other as Video
        return folder.equals(v.folder) && count.equals(v.count)
    }

    override fun hashCode(): Int {
        var str = folder + count
        return str.hashCode()
    }

    override fun toString(): String {
        return "Video(folder=$folder, count=$count)"
    }
}
