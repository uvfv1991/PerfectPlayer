package com.example.perfectplayer.manager

/**
 *  author : jiangxue
 *  date : 2023/8/1 13:52
 *  description :
 */
interface IFragmentCallback {
   // slider的值  是否单文件循环播放 是否以目录循环
    fun callAct(str: Float,onlyfile:Boolean,dirFile:Boolean)

}