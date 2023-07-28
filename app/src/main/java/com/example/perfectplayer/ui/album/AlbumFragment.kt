package com.example.perfectplayer.ui.album

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.blankj.utilcode.util.ToastUtils
import com.example.kotlin.ui.VideoDetailActivity
import com.example.perfectplayer.R
import com.example.perfectplayer.adapter.AlbumAdapter
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.utils.ToActivityHelper
import kotlinx.android.synthetic.main.fragment_album.rv_album

/**
 *  author : jiangxue
 *  date : 2023/7/27 10:25
 *  description :本地相册视频
 */
class AlbumFragment: BaseFragment<AlbumViewModel, ViewDataBinding>() {


//可以分为两类 未选择和选择后的数据 互相覆盖显示就行
    override fun layoutId(): Int {
        return R.layout.fragment_album
    }

    override fun initView(savedInstanceState: Bundle?) {
initRecycle()
    }
    private var albumAdapter: AlbumAdapter? = null
    private fun initRecycle() {
        albumAdapter=AlbumAdapter(viewModel.dataList)
        rv_album?.layoutManager =
           LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        rv_album.adapter =albumAdapter

        rv_album.setItemAnimator(DefaultItemAnimator())
        albumAdapter.apply{

            this?.setOnItemClickListener { adapter, _, position ->
                ToastUtils.showShort("OnItemClickListener")
                when {
                    viewModel.currentType.value == TYPE_NO -> {
                      viewModel.getAlbumList()

                        //获取本地所有视频并列表显示出来
                    }
                    viewModel.currentType.value == TYPE_LIST-> {

                        var bundle = Bundle()
                        var model = data[position] as Video
                        bundle.putSerializable(
                            "detail", model
                        )
                       //播放已经选中的视频
                        ToActivityHelper.getInstance()!!.toActivity(
                            activity!!,
                            VideoDetailActivity::class.java, bundle
                        )

                    }
                }
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun observe() {
        super.observe()
        viewModel.currentType.observe(this,Observer{type->
            when(type){
                TYPE_NO -> {//获取标题
                    albumAdapter?.notifyDataSetChanged()
                }
                TYPE_LIST -> {//获取列表

                    albumAdapter?.setNewInstance(viewModel.dataList)
                    rv_album.adapter=albumAdapter
                    albumAdapter?.notifyDataSetChanged()
                }
            }
        })




        if (viewModel.dataList.isEmpty()) {
            viewModel.getTitle()
        }
    }


    companion object {
        const val TYPE_NO = 0
        const val TYPE_LIST = 1
    }
}