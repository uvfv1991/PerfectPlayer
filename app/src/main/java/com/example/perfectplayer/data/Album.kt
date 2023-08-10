package com.example.perfectplayer.data

import com.google.gson.annotations.SerializedName

public class Album(
    @SerializedName("videoicon") var videoicon: String?,
    @SerializedName("name") var videoName: String,
    @SerializedName("count") var count: String,
) {
    @Transient val id = 0
    var albumId = 0
}
