package com.example.perfectplayer.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aleyn.mvvm.base.BaseFragment
import com.example.perfectplayer.R
import com.example.perfectplayer.ui.album.AlbumViewModel

class SlideshowFragment : BaseFragment<SlideshowViewModel, ViewDataBinding>() {


    override fun layoutId(): Int {
        return R.layout.fragment_slideshow
    }
}