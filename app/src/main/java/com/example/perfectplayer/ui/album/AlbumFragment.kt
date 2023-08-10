package com.example.perfectplayer.ui.album

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.blankj.utilcode.util.FileUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.example.kotlin.ui.VideoDetailActivity
import com.example.perfectplayer.MainActivity
import com.example.perfectplayer.R
import com.example.perfectplayer.adapter.AlbumAdapter
import com.example.perfectplayer.data.Option
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.event.MessageEvent
import com.example.perfectplayer.utils.ToActivityHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.android.synthetic.main.fragment_album.checkall
import kotlinx.android.synthetic.main.fragment_album.rl_notbottom
import kotlinx.android.synthetic.main.fragment_album.rv_album
import kotlinx.android.synthetic.main.fragment_album.toolbar
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule


/**
 *  author : jiangxue
 *  date : 2023/7/27 10:25
 *  description :本地相册视频
 */
class AlbumFragment: BaseFragment<AlbumViewModel, ViewDataBinding>(),OnClickListener {
      var re_menu: MenuItem? =null
    val dataList = ArrayList<Video>()
    private var lastTime: Long = 0
    var isshow = false
    var w:ImageView?=null
    lateinit var refresh:MenuItem
     var behavior: BottomSheetBehavior<RelativeLayout>? = null

    override fun layoutId(): Int {
        return R.layout.fragment_album
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        if (isAdded){
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24, null)
            val mActivity = activity as AppCompatActivity
            mActivity.setSupportActionBar(toolbar)
            var actionBar : ActionBar? = mActivity.supportActionBar
// enabling action bar app icon and behaving it as toggle button
            actionBar?.setDisplayHomeAsUpEnabled(true);
            actionBar?.setHomeButtonEnabled(true);
            behavior = BottomSheetBehavior.from(rl_notbottom)
            behavior!!.setSkipCollapsed(true)
            behavior!!.setHideable(true)
            behavior!!.setState(STATE_HIDDEN)
            initRecycle()
            initEvent()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initEvent() {

        //设置监听事件
        behavior?.setBottomSheetCallback(object : BottomSheetCallback() {
            var hasRequest = false
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //状态变化
              /*  if (!hasRequest && behavior!!.getPeekHeight() == 0 && slideOffset > 0) {
                    hasRequest = true;
                    updateOffsets(bottomSheet);
                }*/
            }
        })
        toolbar.setNavigationOnClickListener {
            if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED){
                behavior!!.setHideable(true)
                behavior!!.setState(STATE_HIDDEN)
                albumAdapter?.isshowcheck(false)
            }


            if (behavior?.state  == BottomSheetBehavior.STATE_HIDDEN && type==0){
                val mActivity = activity as MainActivity
                mActivity.doubleClickExit()

            }else if (type==1){
              viewModel.getTitle()
            }
        }

        checkall.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                albumAdapter?.selecthall(true)
            } else {
                albumAdapter?.selecthall(false)
            }
        })

        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {

            when(it.itemId){
                R.id.menuhistory -> {
                    toast("历史记录")
                }

            }
            false

        })
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
                when {
                    viewModel.currentType.value == TYPE_NO -> {
                      viewModel.getAlbumList(viewModel.dataList.get(position).folder)
                    }
                    viewModel.currentType.value == TYPE_LIST-> {

                        var bundle = Bundle()
                        var model = data[position] as Video
                        bundle.putSerializable(
                            "position", position
                        )
                        bundle.putSerializable( //传递所有数据
                            "folder", viewModel.dataList
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
        albumAdapter!!.setOnItemLongClickListener(object : OnItemLongClickListener {

            override fun onItemLongClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ): Boolean {
                selectFileOption(position)
                //不准向下传递事件
                return true
            }
        })


    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun observe() {
        super.observe()
        viewModel.currentType.observe(this,Observer{type->
           hideRefreshAnimation()
            when(type){
                TYPE_NO -> {//获取标题
                    albumAdapter?.setNewInstance(viewModel.dataList)
                    rv_album.adapter=albumAdapter
                    Companion.type =TYPE_NO.toInt()
                }
                TYPE_LIST -> {//获取列表
                    Companion.type = TYPE_LIST.toInt()
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


    fun selectFileOption(position: Int) {
        val  countries:List<String>
        if (type== TYPE_NO){
        countries = listOf(Option.DUOXUAN.keyname, Option.DELETE.keyname,Option.REFECT.keyname,Option.QXZDPX.keyname)  //传list
        }else {countries = listOf(Option.DUOXUAN.keyname, Option.DELETE.keyname,Option.REFECT.keyname,
                                  Option.QTDKFS.keyname, Option.TJSMLB.keyname, Option.DLNA.keyname,
                                    Option.CKXQ.keyname)}

         selector("选择文件操作", countries, { dialogInterface, i ->
             when(i){
                 Option.DUOXUAN.number->{

                     behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                     albumAdapter?.isshowcheck(true)

                 }
                 Option.DELETE.number->  showDelOption(position)
                 Option.REFECT.number->   if (type== TYPE_NO) toast("亲，不能重命名哈")else showRename(position)
                 Option.QXZDPX.number->  toast("So you're living in ${countries[i]}, right?")
             }

         })

     }

    private fun showRename(position: Int) {
        var text:String?=null
        alert(viewModel.dataList.get(position).folder.toString(), "请输入文件名") {
            yesButton { 
                val rename = FileUtils.rename(viewModel.dataList.get(position).path, text)


            }
            customView {
                //var editText = editText()
            }
            noButton {toast("取消了")}
        }.show()

    }

    fun showDelOption(position: Int) {

        alert(viewModel.dataList.get(position).folder.toString(), "删除文件夹中的视频") {
            yesButton {
                if (type== TYPE_NO) toast("亲，不能删除哈") }
            noButton {}
        }.show()

    }
    @RequiresApi(Build.VERSION_CODES.N)
    @org.greenrobot.eventbus.Subscribe
    fun onEventUpdate(event: MessageEvent) {
        when(event.getType()){
            1->  viewModel.getTitle()
        }


    }

    companion object {
        const val TYPE_NO = 0
        const val TYPE_LIST = 1
        var type = 0
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_all->{ albumAdapter?.isshowcheck(true) }
            R.id.tv_delete->{
            //albumAdapter?.de(true)

            }

        }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK
            && event.action == KeyEvent.ACTION_DOWN
        ) {
            if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED){
                behavior!!.setHideable(true)
                behavior!!.setState(STATE_HIDDEN)
                albumAdapter?.isshowcheck(false)
            }


            if (behavior?.state  == BottomSheetBehavior.STATE_HIDDEN && type==0){
                val mActivity = activity as MainActivity
                mActivity.doubleClickExit()

            }else if (type==1){
                viewModel.getTitle()
            }
            true
        } else false
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        re_menu=item
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onPrepareOptionsMenu(menu: Menu) {

        refresh = menu!!.findItem(R.id.menurefresh)
        w = refresh.actionView  as ImageView

        w?.apply {
            setImageDrawable(refresh.icon)
            w!!.onClick {
                toast("重新扫描中，请稍候")
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_refresh))
                viewModel.getTitle()
            }

        }


    }
    var schedule :TimerTask?=null

    @SuppressLint("NewApi")
    private fun hideRefreshAnimation() {
        schedule = Timer().schedule(6000) { //执行的任务
            w?.clearAnimation()

            Looper.prepare()
            toast("扫描完成")
            Looper.loop()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}