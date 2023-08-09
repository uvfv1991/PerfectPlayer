package com.example.perfectplayer.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.perfectplayer.R
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.ui.album.AlbumFragment
import com.example.perfectplayer.utils.ImageUtils

/**
 *  author : jiangxue
 *  date : 2023/6/8 16:03
 *  description :
 */
class AlbumAdapter(data: MutableList<Video>) :
    BaseMultiItemQuickAdapter<Video, BaseViewHolder>(data) {

    init {
        //为不同类型添加不同的布局
        addItemType(Video.title,R.layout.adapter_video_title)
        addItemType(Video.list , R.layout.adapter_video_list)
    }


    override fun convert(holder: BaseViewHolder, item: Video) {
        val checkBox = holder.getView(R.id.cb_album) as CheckBox
        val layout = holder.getView(R.id.ll) as LinearLayout
        when (holder.itemViewType) {

            AlbumFragment.TYPE_NO -> {//获取标题
                holder.setText(R.id.tv_albumname, item.folder)
                holder.setText(R.id.tv_count, item.count.toString() + "个数")
            }

            AlbumFragment.TYPE_LIST -> {//获取列表
                holder.setText(R.id.tv_videoname, item.videoName)

                ImageUtils.loadImage(holder.getView(R.id.im_icon), item.thumbPath.toString())
            }


        }
        if (item.isshow) {
            holder.setGone(R.id.cb_album, false)
        } else {
            holder.setGone(R.id.cb_album, true)
        }

        if (item.ischeck) {

            checkBox.setChecked(true)
        } else {
            checkBox.setChecked(false)
        }
        checkBox.setOnClickListener(View.OnClickListener {
            if (checkBox.isChecked()) {
                item.ischeck = true
            } else {
                item.ischeck = false
            }
        })


    }

    //处理全选控件
    public fun selecthall(b: Boolean) {
        val iterator: Iterator<Video> = data.iterator()
        while (iterator.hasNext()) {
            val video: Video = iterator.next()
            if (b) {
                if (!video.ischeck) {
                    video.ischeck = true
                }
            } else {
                if (video.ischeck) {
                    video.ischeck = false
                }
            }
        }
        notifyDataSetChanged()
    }

    //控制cb的显示
    fun isshowcheck(b: Boolean) {
        val iterator: Iterator<Video> = data.iterator()
        while (iterator.hasNext()) {
            val video: Video = iterator.next()
            if (b) {
                if (!video.isshow) {
                    video.isshow = true
                }
            } else {
                if (video.isshow) {
                    video.isshow = false
                }
            }
        }
        notifyDataSetChanged()
    }

}