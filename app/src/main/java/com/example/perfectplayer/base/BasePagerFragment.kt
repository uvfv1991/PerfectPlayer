package com.example.perfectplayer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.base.BaseViewModel
import com.example.perfectplayer.R
import com.example.perfectplayer.adapter.BaseFragmentPagerAdapter
import com.example.perfectplayer.databinding.FragmentBasePagerBinding
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

/**
 *  author : jiangxue
 *  date : 2023/8/4 16:42
 *  description :
 */
abstract class BasePagerFragment :
    BaseFragment<BaseViewModel,FragmentBasePagerBinding>() {
    private var mFragments: List<Fragment>? = null
    private var titlePager: List<String>? = null
    protected abstract fun pagerFragment(): List<Fragment>?
    protected abstract fun pagerTitleString(): List<String>?

    override fun initData() {
        mFragments = pagerFragment()
        titlePager = pagerTitleString()
        //设置Adapter
        val pagerAdapter =
            BaseFragmentPagerAdapter(getChildFragmentManager(), mFragments, titlePager)
    }

}