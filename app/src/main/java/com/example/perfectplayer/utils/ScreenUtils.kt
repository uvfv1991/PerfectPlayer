package com.example.perfectplayer.utils

import android.content.Context
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 *  author : jiangxue
 *  date : 2023/8/7 11:56
 *  description :
 */
public class ScreenUtils {





    companion object {
        //该方法用于dp To px
        fun dpToPx(context: Context, dp: Int): Int {

            return (dp * context.getResources().getDisplayMetrics().density).toInt()
        }


        fun getFloatNoMoreThanOneDigits(number: Float): String {
            val format = DecimalFormat("#.##")
            //舍弃规则，RoundingMode.FLOOR表示直接舍弃。
            format.roundingMode = RoundingMode.FLOOR
            return format.format(number)
        }

    }
}