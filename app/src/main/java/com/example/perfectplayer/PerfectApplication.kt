package com.example.perfectplayer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.perfectplayer.manager.TTAdManagerHolder
import org.litepal.LitePal

class PerfectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LitePal.initialize(this)
        // 穿山甲SDK初始化
        // 强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
