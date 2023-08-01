package com.example.perfectplayer.ui.view

import android.annotation.TargetApi
import android.app.ActionBar
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import com.example.perfectplayer.R
import kotlinx.android.synthetic.main.layout_custom_toolbar.view.iv_back
import kotlinx.android.synthetic.main.layout_custom_toolbar.view.iv_refresh


/**
 *  author : jiangxue
 *  date : 2023/7/31 9:56
 *  description :
 */
class CustomToolBar:Toolbar{


    private lateinit var mView: View
    private lateinit var tv_title: TextView

    constructor(context: Context) : super(context) {
            initView()

        }

        constructor(context:Context, attr: AttributeSet) : super(context, attr) {
            initView()
        }


        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs,defStyleAttr)  {
            initView()
            initAttr(attrs,defStyleAttr)
        }

    private fun initAttr(attrs: AttributeSet?, defStyleAttr: Int) {

    }


    /**
     * 填充 layout
     */
    private fun initView() {

            var mInflater = LayoutInflater.from(context)
            mView =
                mInflater.inflate(R.layout.layout_custom_toolbar, null)
            var iv_back = mView.findViewById(R.id.iv_back) as ImageView
                tv_title = mView.findViewById(R.id.tv_title) as TextView
            var iv_refresh = mView.findViewById(R.id.iv_refresh) as ImageView
            var iv_history = mView.findViewById(R.id.iv_history) as ImageView
            //设置 params
            val lp = ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL
            )
            addView(mView, lp)

    }


    fun setMainText(text: CharSequence) {
        tv_title.setText(text)
        tv_title.setVisibility(VISIBLE)
    }


     fun setLeftIcon(icon: Drawable) {
        if (iv_back != null) {
            iv_back.setBackground(icon)

        }
    }

    fun setRefreshIcon(icon: Drawable) {
        if (iv_refresh != null) {
            iv_refresh.setBackground(icon)
        }
    }
}