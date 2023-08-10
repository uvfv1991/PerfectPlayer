package com.example.perfectplayer.ui.video

import androidx.fragment.app.Fragment
import com.example.perfectplayer.R
import com.example.perfectplayer.base.BasePagerFragment

/**
 *  author : jiangxue
 *  date : 2023/8/4 16:41
 *  description :
 */
class VideoSettingFragment : BasePagerFragment() {
    protected override fun pagerFragment(): List<Fragment> {
        val list: MutableList<Fragment> = ArrayList()
        list.add(Video_Fragment())
        list.add(Video_Fragment())
        list.add(Video_Fragment())
        list.add(Video_Fragment())
        return list
    }

    protected override fun pagerTitleString(): List<String> {
        val list: MutableList<String> = ArrayList()
        list.add("视频")
        list.add("音频")
        list.add("字幕")
        list.add("其他")
        return list
    }

    override fun layoutId(): Int {
        return R.layout.fragment_base_pager
    }
}
