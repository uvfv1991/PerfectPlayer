package com.example.perfectplayer.base

import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 *  author : jiangxue
 *  date : 2023/8/1 14:52
 *  description :
 */
open class BaseActivity:AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }
}