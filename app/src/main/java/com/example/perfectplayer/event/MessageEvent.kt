package com.example.perfectplayer.event

/**
 *  author : jiangxue
 *  date : 2023/8/1 13:20
 *  description :
 */

class MessageEvent internal constructor(message: String) {

    private var message: String? = null
    init {
        this.message = message
    }
    internal fun getMessage(): String? {
        return message
    }
    fun setMessage(message: String) {
        this.message = message
    }
}

