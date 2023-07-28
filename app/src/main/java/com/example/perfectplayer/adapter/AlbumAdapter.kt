package com.example.perfectplayer.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.perfectplayer.R
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.ui.album.AlbumFragment
import com.example.perfectplayer.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_album.rv_album

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

        when(holder.itemViewType){

            AlbumFragment.TYPE_NO -> {//获取标题
                holder.setText(R.id.tv_albumname, item.videoName)
                holder.setText(R.id.tv_count, item.count.toString()+"个数")
            }
            AlbumFragment.TYPE_LIST -> {//获取列表
                holder.setText(R.id.tv_videoname, item.videoName)

                ImageUtils.loadImage(holder.getView(R.id.im_icon), item.thumbPath.toString())
            }

        }

    }
}