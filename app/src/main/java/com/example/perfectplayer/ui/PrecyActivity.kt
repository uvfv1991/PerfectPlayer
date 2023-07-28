package com.example.perfectplayer.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.base.NoViewModel
import com.blankj.utilcode.util.ResourceUtils
import com.example.perfectplayer.MainActivity
import com.example.perfectplayer.R
import com.example.perfectplayer.databinding.ActivityPracyBinding

class PrecyActivity : BaseActivity<NoViewModel, ActivityPracyBinding>() {

    var dialog: Dialog? = null

    fun onClickAgree(v: View?) {

        dialog!!.dismiss()
        //下面将已阅读标志写入文件，再次启动的时候判断是否显示。
        getSharedPreferences("file", Context.MODE_PRIVATE).edit()
            .putBoolean("agree", true)
            .apply()
    }

    fun onClickDisagree(v: View?) {
        System.exit(0)
    }

    fun showPrivacy(privacyFileName: String?) {
        val str =  ResourceUtils.readAssets2String(privacyFileName)
        val inflate: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_privacy_show, null)
        val tv_title = inflate.findViewById<View>(R.id.tv_title) as TextView
        tv_title.text = "隐私政策授权提示"
        val tv_content =
            inflate.findViewById<View>(R.id.tv_content) as TextView
        tv_content.text = str
        dialog = AlertDialog.Builder(this)
            .setView(inflate)
            .show()
        // 通过WindowManager获取
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val params = (dialog as AlertDialog?)?.window?.attributes
        if (params != null) {
            params.width  = dm.widthPixels * 4 / 5
            params.height = dm.heightPixels * 1 / 2
        }
        (dialog as AlertDialog?)?.setCancelable(false) //屏蔽返回键
        (dialog as AlertDialog?)?.window?.attributes  = params
        // (dialog as AlertDialog?)?.window?.setBackgroundDrawableResource(R.color.transparent)

    }

    //隐私检查
    fun PravicyCheck() {
        val status =
            getSharedPreferences("file", Context.MODE_PRIVATE)
                .getBoolean("agree", false)
        if (status == true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showPrivacy("privacy.txt")
        }

    }

    override fun layoutId(): Int {
        return  R.layout.activity_pracy
    }

    override fun initView(savedInstanceState: Bundle?) {
        PravicyCheck()
    }

    override fun initData() {
    }
}

