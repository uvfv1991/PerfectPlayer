package com.example.perfectplayer.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
