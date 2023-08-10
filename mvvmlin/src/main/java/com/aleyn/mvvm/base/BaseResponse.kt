package com.aleyn.mvvm.base

/**
 *  author : jiangxue
 *  date : 2023/6/16 13:53
 *  description :
 */
data class BaseResponse<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T,
) : IBaseResponse<T> {

    override fun code() = errorCode

    override fun msg() = errorMsg

    override fun data() = data

    override fun isSuccess() = errorCode == 0
}
