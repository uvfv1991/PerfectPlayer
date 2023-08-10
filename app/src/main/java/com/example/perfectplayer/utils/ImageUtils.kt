package com.example.perfectplayer.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.perfectplayer.R
import com.example.perfectplayer.manager.BlurTransformation

class ImageUtils {

    companion object {

        /**
         * 高斯模糊图片
         */
        fun loadImageBlur(imageview: ImageView, url: String) {
            Glide.with(imageview.context)
                .load(url)
                .apply(
                    RequestOptions.bitmapTransform(
                        BlurTransformation(imageview.context),
                    ),
                )
                .into(imageview)
        }

        fun loadImageCircle(imageview: ImageView, url: String) {
            if (TextUtils.isEmpty(url)) {
                return
            }

            Glide.with(imageview.context)
                .load(url)
                .apply(
                    RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher)
                        .error(ColorDrawable(Color.WHITE)).fallback(ColorDrawable(Color.RED)),
                ).into(imageview)
        }

        fun loadImage(imageview: ImageView, url: String) {
            Glide.with(imageview.context)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .frame(3000000)
                        .centerCrop(),
                ).load(url).into(imageview)
        }
    }
}
