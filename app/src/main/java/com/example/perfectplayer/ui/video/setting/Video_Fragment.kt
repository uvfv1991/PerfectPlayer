package com.example.perfectplayer.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.perfectplayer.R
import com.example.perfectplayer.base.BasePagerFragment
import com.example.perfectplayer.ui.gallery.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_album.rv_album

/**
 *  author : jiangxue
 *  date : 2023/8/4 16:41
 *  description :
 */
class Video_Fragment :  Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(this@Video_Fragment, Observer {
            textView.text ="我是视频"
        })
        return root
    }
    /**
     * scroll to top
     */
    fun smoothScrollToPosition() = rv_album.scrollToPosition(0)
}