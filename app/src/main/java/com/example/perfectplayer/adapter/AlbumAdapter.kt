package com.example.perfectplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.perfectplayer.R
import com.example.perfectplayer.data.Album

/**
 *  author : jiangxue
 *  date : 2023/6/8 16:03
 *  description :
 */
class AlbumAdapter(layoutResId: Int, data: MutableList<Album>) : BaseQuickAdapter<Album, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: Album) {
        holder.setText(R.id.tv_albumname, item.videoName)
        holder.setText(R.id.tv_count, item.count)

        /* Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .centerCrop()
        ).load(item?.data?.image).into(helper.getView<View>(R.id.im_cover) as ImageView)
    }*/
    }
}