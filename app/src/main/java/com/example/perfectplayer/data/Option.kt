package com.example.perfectplayer.data


/**
 *  author : jiangxue
 *  date : 2023/8/1 11:18
 *  description :
 */
enum class Option(val number: Int, val keyname: String) {
    DUOXUAN(1,"多选"),
    DELETE(2, "删除"),
    REFECT(3, "重命名"),
    QXZDPX(4, "取消排序置顶");


    fun getKeyName(day: Option): String{
        return day.keyname
    }


}