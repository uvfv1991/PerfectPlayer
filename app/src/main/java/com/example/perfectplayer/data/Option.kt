package com.example.perfectplayer.data


/**
 *  author : jiangxue
 *  date : 2023/8/1 11:18
 *  description :
 */
enum class Option(val number: Int, val keyname: String) {
    DUOXUAN(0,"多选"),
    DELETE(1, "删除"),
    REFECT(2, "重命名"),
    QXZDPX(3, "取消排序置顶"),
    QTDKFS(4, "其他打开方式"),
    TJSMLB(5, "添加到私密播放列表"),
    DLNA(6, "DLNA投屏播放"),
    CKXQ(6, "查看详情");


    fun getKeyName(day: Option): String{
        return day.keyname
    }


}