package com.example.perfectplayer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.base.BaseViewModel
import com.blankj.utilcode.util.ScreenUtils
import com.example.perfectplayer.R
import com.example.perfectplayer.adapter.BaseFragmentPagerAdapter
import com.example.perfectplayer.databinding.FragmentBasePagerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

/**
 *  author : jiangxue
 *  date : 2023/8/4 16:42
 *  description :
 */
abstract class BaseSheetPagerFragment :
    BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



}