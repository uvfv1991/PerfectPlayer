package com.example.perfectplayer.manager

import android.content.Context
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdManager
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTAdSdk.InitCallback

/**
 *  author : jiangxue
 *  date : 2023/8/11 13:33
 *  description :
 */
/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
object TTAdManagerHolder {
    private const val TAG = "TTAdManagerHolder"
    private var sInit = false
    fun get(): TTAdManager {
        return TTAdSdk.getAdManager()
    }

    fun init(context: Context) {
        // 初始化穿山甲SDK
        doInit(context)
    }

    // step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private fun doInit(context: Context) {
        if (!sInit) {
            // TTAdSdk.init(context, buildConfig(context));
            TTAdSdk.init(
                context,
                buildConfig(context),
                object : InitCallback {
                    override fun success() {
                        Log.i(TAG, "success: " + TTAdSdk.isInitSuccess())
                    }

                    override fun fail(code: Int, msg: String) {
                        Log.i(TAG, "fail:  code = $code msg = $msg")
                    }
                },
            )
            sInit = true
        }
    }

    private fun buildConfig(context: Context): TTAdConfig {
        return TTAdConfig.Builder()
            .appId("5425688")
            .useTextureView(true)
            .allowShowNotify(true)
            .debug(true)
            .directDownloadNetworkType(
                TTAdConstant.NETWORK_STATE_WIFI,
                TTAdConstant.NETWORK_STATE_3G,
            ) // 允许直接下载的网络状态集合
            .supportMultiProcess(false) // 是否支持多进程
            .needClearTaskReset()
            .build()
    }
}
