package com.example.kotlin.ui

 import android.content.Intent
 import android.content.pm.ActivityInfo
 import android.content.res.Configuration
 import android.graphics.Bitmap
 import android.graphics.Canvas
 import android.graphics.Color
 import android.os.Build
 import android.os.Bundle
 import android.util.Log
 import android.view.MenuItem
 import android.view.View
 import android.view.WindowManager
 import androidx.media3.common.C
 import androidx.media3.common.MediaItem
 import androidx.media3.common.MediaMetadata
 import androidx.media3.common.PlaybackException
 import androidx.media3.common.PlaybackParameters
 import androidx.media3.common.Player
 import androidx.media3.common.Player.PLAYBACK_SUPPRESSION_REASON_NONE
 import androidx.media3.common.Player.REPEAT_MODE_ALL
 import androidx.media3.common.Player.REPEAT_MODE_ONE
 import androidx.media3.common.Tracks
 import androidx.media3.common.VideoSize
 import androidx.media3.exoplayer.ExoPlayer
 import androidx.media3.ui.AspectRatioFrameLayout
 import androidx.media3.ui.PlayerView
 import com.aleyn.mvvm.base.BaseActivity
 import com.aleyn.mvvm.base.NoViewModel
 import com.blankj.utilcode.util.ImageUtils
 import com.example.perfectplayer.R
 import com.example.perfectplayer.common.Constant.Companion.KEY_ITEM_INDEX
 import com.example.perfectplayer.common.Constant.Companion.KEY_POSITION
 import com.example.perfectplayer.data.Video
 import com.example.perfectplayer.databinding.ActivityVideoDetailBinding
 import com.example.perfectplayer.manager.IFragmentCallback
 import com.example.perfectplayer.ui.album.SettingFragment.SettingBottomSheetFragment
 import kotlinx.android.synthetic.main.activity_video_detail.playerView
 import kotlinx.android.synthetic.main.activity_video_detail.titleText
 import kotlinx.android.synthetic.main.activity_video_detail.tv_moreScale
 import kotlinx.android.synthetic.main.adapter_video_title.tv_albumname
 import org.jetbrains.anko.contentView
 import org.jetbrains.anko.toast
 import java.util.Formatter
 import java.util.Locale


class VideoDetailActivity : BaseActivity<NoViewModel, ActivityVideoDetailBinding>(),
    PlayerView.ControllerVisibilityListener,IFragmentCallback{
    protected final var TAG : String = "VideoDetailActivity";
    private var isPlaying = false
    var player: ExoPlayer? =null
    lateinit var video:Video
     var pos:Int =0
    var dataList = ArrayList<Video>()
    var mediaItems: MutableList<MediaItem> = ArrayList()//播放本地视频
    private var startItemIndex = 0
    var listener: Player.Listener?=null
    override fun layoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        var intent = intent
        //video = intent.getSerializableExtra("detail") as Video
        pos = intent.getIntExtra("position",0)
        dataList = intent.getSerializableExtra("folder") as ArrayList<Video>
        Log.e(TAG,"url"+dataList.get(pos).path)
        initPlayer()
    }

    private fun createMediaItems(): List<MediaItem>? {


        dataList.forEach { mediaItems.add(MediaItem.fromUri(it.path.toString())) }
        return mediaItems
    }

    private fun initPlayer() {
        if (player == null) {
            //1. 构建播放器实例
            player = ExoPlayer.Builder(this).build()

            playerView.player=player
            val mediaItems = createMediaItems()

            if (mediaItems == null  ||  mediaItems.isEmpty()) {
                return
            }
            player!!.setMediaItems(mediaItems,  /* resetPosition= */false)
            player?.prepare()
        }

        //Play from the item selected on the playlist from previous activity/fragment
        player?.seekTo(pos, C.TIME_UNSET);
        player?.addListener(PlayerEventListener())
        player?.playWhenReady = true
    }

    /*视频编码指的就是画面图像的编码压缩方式，一般有 H263、H264、HEVC（H265）、MPEG-2 、MPEG-4 等，
    其中H264 是目前比较常见的编码方式。

    * 视频编码的存在，视频编码的作用就是对传输图片进行压缩，从而达到尽量还原画面的同时，得到更小的体积。*/

    //是否支持软解
    fun isSoftwareCodec(codecName: String): Boolean {
        if (codecName.startsWith("OMX.google.")) {
            return true
        }
        return if (codecName.startsWith("OMX.")) {
            false
        } else true
    }

    fun onClickSetting(v: View?) {
        //打开设置
       SettingBottomSheetFragment().show(supportFragmentManager, "settingdialog")
    }

    fun onClickCut(v: View?) {
        //开始截个图并存储到手机相册
        val screenshotOfView =
            takeScreenshotOfView(contentView!!, contentView!!.height, contentView!!.width)

        val file = ImageUtils.save2Album(screenshotOfView, Bitmap.CompressFormat.PNG, false)
        if (ImageUtils.isImage(file)){
            toast("截图成功，保存为"+ (file?.path))
        }
    }
    private var isFullscreen = false
    //是否播放过
    protected var mHadPlay = false

    //记住切换数据源类型
    private var mType = 0
    //切换清晰度
    fun onClickMoreScale(v: View?) {
        when(mType){
            0->mType = 1
            1->mType = 2

        }
        resolveTypeUI()

    }

    private fun showLock() {

       //playerView.hideController()

        //playerView.showController()
    }

    private fun resolveTypeUI() {
        when(mType){
            1->{
                tv_moreScale.setText("全屏")
                isFullscreen=true
            }

            2->{
                tv_moreScale.setText("默认比例")
                isFullscreen=false
            }

        }
        toggleFullscreen()

    }
    override fun initData() {
        initListener()
    }



    //播放器监听
    private fun initListener() {



    }

    inner class PlayerEventListener : Player.Listener {

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            super.onVideoSizeChanged(videoSize)
            Log.e("video", "onVideoSizeChanged")
        }
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            Log.e("video", "onPlayWhenReadyChanged")
        }
        override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
            Log.e("video", "onPlaybackStateChanged")
            when (playbackState) {
                Player.STATE_ENDED -> {
                    Log.e("video", "播放完成")
                }// 可以被播放状态}//播放结束
                Player.STATE_BUFFERING -> {}// 正在缓冲
                Player.STATE_IDLE -> {}// 空闲状态
                Player.STATE_READY -> {
                    //Log.e("video","当前播放状态"+player?)
                }// 可以被播放状态
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player!!.seekToDefaultPosition()
                player!!.prepare()
            } else {
                //updateButtonVisibility()
                //showControls()
            }
        }
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            //Log.e("video", "2"+dataList.get(reason).videoName)
            //titleText.text=dataList.get(reason).videoName
        }


        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            Log.e("video", "4"+dataList.get(newPosition.mediaItemIndex).videoName)
            titleText.text=dataList.get(newPosition.mediaItemIndex).videoName
        }

    }

    private fun stringForTime(timeMs: Int): String? {
        val mFormatBuilder: StringBuilder
        val mFormatter: Formatter
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        releasePlayer()
        clearStartPosition()
        setIntent(intent)
    }
    protected fun clearStartPosition() {
        startItemIndex = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT <= 23 || player == null) {
            initPlayer()
            if (playerView != null) {
                playerView.onResume()
            }
        }
    }

    //        注意：在使用ExoPlayer时，需要在Activity中保持屏幕常亮以避免视频播放过程中屏幕自动关闭。
    override fun onResume() {
        super.onResume()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT <= 23 || player == null) {
            initPlayer()
            if (playerView != null) {
                playerView.onResume()
            }
        }


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            titleText.text=dataList.get(pos).videoName
            titleText.visibility=View.VISIBLE
        }else{
            titleText.visibility=View.GONE
        }
    }


    override fun onPause() {
        super.onPause()
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause()
            }
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            initPlayer()
            if (playerView != null) {
                playerView.onPause()
            }
            releasePlayer()
        }
    }
    private var startPosition: Long = 0
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateStartPosition()

        outState.putInt(KEY_ITEM_INDEX,
            startItemIndex
        )
        outState.putLong(KEY_POSITION,
            startPosition
        )

    }

    private fun updateStartPosition() {
        if (player != null) {
            startItemIndex = player!!.currentMediaItemIndex
            startPosition = Math.max(0, player!!.contentPosition)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }



    override fun onVisibilityChanged(visibility: Int) {
        TODO("Not yet implemented")

    }

    fun onClickMenuOrBack(v: View?) {
        finish()

    }
    override fun callAct(str: Float, onlyfile: Boolean, dirFile: Boolean) {
       // 设置播放速度和音调均为2倍速
        player?.playbackParameters =PlaybackParameters(str,1F)
        if (onlyfile)player?.repeatMode =REPEAT_MODE_ONE

        if (dirFile)player?.repeatMode = REPEAT_MODE_ALL

        player?.play()
    }




    //设置是滞全屏
    private fun toggleFullscreen() {
        if (isFullscreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
        }
        isFullscreen = !isFullscreen

    }





    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }



    fun releasePlayer() {
        if (player != null) {

            player!!.release()
            player = null
            playerView.player = null
            mediaItems.clear()
        }
    }

}



