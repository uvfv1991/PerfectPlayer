package com.example.perfectplayer.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.base.NoViewModel
import com.example.perfectplayer.R
import com.example.perfectplayer.databinding.ActivityPracyBinding
import java.io.*

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
        System.exit(0) //退出软件
    }

    fun showPrivacy(privacyFileName: String?) {
        val str = initAssets(privacyFileName)
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



    fun PravicyCheck() {
        val status =
            getSharedPreferences("file", Context.MODE_PRIVATE)
                .getBoolean("agree", false)
        if (status == true) {

        } else {
            showPrivacy("privacy.txt") //放在assets目录下的隐私政策文本文件
        }
    }
    /**
     * 从assets下的txt文件中读取数据
     */
    fun initAssets(fileName: String?): String? {
        var str: String? = null
        try {
            val inputStream = assets.open(fileName)
            str = getString(inputStream)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return str
    }
    companion object {
        fun getString(inputStream: InputStream?): String {
            var inputStreamReader: InputStreamReader? = null
            try {
                inputStreamReader = InputStreamReader(inputStream, "UTF-8")
            } catch (e1: UnsupportedEncodingException) {
                e1.printStackTrace()
            }
            val reader = BufferedReader(inputStreamReader)
            val sb = StringBuffer("")
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                    sb.append("\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return sb.toString()
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

