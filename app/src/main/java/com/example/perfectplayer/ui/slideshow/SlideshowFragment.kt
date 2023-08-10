package com.example.perfectplayer.ui.slideshow

import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseFragment
import com.example.perfectplayer.R

class SlideshowFragment : BaseFragment<SlideshowViewModel, ViewDataBinding>() {

    override fun layoutId(): Int {
        return R.layout.fragment_slideshow
    }
}
