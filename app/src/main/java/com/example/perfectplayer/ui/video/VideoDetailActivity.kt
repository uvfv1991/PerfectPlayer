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
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.TimeUtils
import com.example.perfectplayer.R
import com.example.perfectplayer.common.Constant.Companion.KEY_ITEM_INDEX
import com.example.perfectplayer.common.Constant.Companion.KEY_POSITION
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.databinding.ActivityVideoDetailBinding
import com.example.perfectplayer.manager.IFragmentCallback
import com.example.perfectplayer.ui.album.AlbumFragment
import com.example.perfectplayer.ui.album.SettingFragment.SettingBottomSheetFragment
import com.example.perfectplayer.ui.video.VideoDetailModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_video_detail.playerView
import kotlinx.android.synthetic.main.activity_video_detail.titleText
import kotlinx.android.synthetic.main.activity_video_detail.tv_time
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat

class VideoDetailActivity :
    BaseActivity<VideoDetailModel, ActivityVideoDetailBinding>(),
    IFragmentCallback {

    var player: ExoPlayer? = null
    lateinit var video: Video
    var pos: Int = 0
    var dataList = ArrayList<Video>()
    var mediaItems: MutableList<MediaItem> = ArrayList() // 播放本地视频
    private var startItemIndex = 0
    var listener: Player.Listener? = null
    override fun layoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        var intent = intent
        pos = intent.getIntExtra("position", 0)
        dataList = intent.getSerializableExtra("folder") as ArrayList<Video>
        tv_time.text = viewModel.getTime()
        initPlayer()
    }

    private fun createMediaItems(): List<MediaItem>? {
        dataList.forEach { mediaItems.add(MediaItem.fromUri(it.path.toString())) }
        return mediaItems
    }

    private fun initPlayer() {
        if (player == null) {
            // 1. 构建播放器实例
            player = ExoPlayer.Builder(this).build()

            playerView.player = player

            player!!.setMediaItem(MediaItem.fromUri(dataList.get(pos).path.toString()))
            player?.prepare()
            player?.repeatMode = REPEAT_MODE_OFF
        }

        // Play from the item selected on the playlist from previous activity/fragment
        // player?.seekTo(pos, C.TIME_UNSET)
        player?.addListener(PlayerEventListener())
        player?.playWhenReady = true
    }

    /*视频编码指的就是画面图像的编码压缩方式，一般有 H263、H264、HEVC（H265）、MPEG-2 、MPEG-4 等，
    其中H264 是目前比较常见的编码方式。

    * 视频编码的存在，视频编码的作用就是对传输图片进行压缩，从而达到尽量还原画面的同时，得到更小的体积。*/

    fun onClickSetting(v: View?) {
        // 打开设置
        SettingBottomSheetFragment().show(supportFragmentManager, "settingdialog")
    }

    fun onClickCut(v: View?) {
        // 开始截个图并存储到手机相册
        val screenshotOfView =
            takeScreenshotOfView(contentView!!, contentView!!.height, contentView!!.width)

        val file = ImageUtils.save2Album(screenshotOfView, Bitmap.CompressFormat.PNG, false)
        if (ImageUtils.isImage(file)) {
            toast("截图成功，保存为" + (file?.path))
        }
    }
    private var isFullscreen = false

    // 切换清晰度
    fun onClickMoreScale(v: View?) {
        toggleFullscreen()
    }

    override fun initData() {
        initListener()
    }

    // 切换引擎
    fun onClickYQ(v: View?) {
        toast("如果遇到播放问题，可以在首页设置中切换到另一个引擎试试")
    }

    // 播放器监听
    private fun initListener() {
    }

    inner class PlayerEventListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
            Log.e("video", "onPlaybackStateChanged")
            when (playbackState) {
                Player.STATE_ENDED -> {
                    Log.e("video", "播放完成")
                    val video = dataList.get(pos)
                    video.type = AlbumFragment.TYPE_History
                    var format = SimpleDateFormat("HH:mm")
                    video.date = TimeUtils.getNowString(format)
                    viewModel.saveHistory(video)
                } // 可以被播放状态}//播放结束
                Player.STATE_BUFFERING -> {} // 正在缓冲
                Player.STATE_IDLE -> {} // 空闲状态
                Player.STATE_READY -> {
                    // Log.e("video","当前播放状态"+player?)
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            // 首次选择目录循环后添加列表文件
            val mediaItems = createMediaItems()

            if (mediaItems == null || mediaItems.isEmpty()) {
                return
            }
            player!!.setMediaItems(mediaItems)
        }

        override fun onPlayerError(error: PlaybackException) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player!!.seekToDefaultPosition()
                player!!.prepare()
            } else {
            }
        }
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int,
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            Log.e("video", "4" + dataList.get(newPosition.mediaItemIndex).videoName)
            titleText.text = dataList.get(newPosition.mediaItemIndex).videoName
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
        if (Build.VERSION.SDK_INT > 23) {
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
        titleText.text = dataList.get(pos).videoName
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            titleText.visibility = View.VISIBLE
        } else {
            titleText.visibility = View.GONE
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

        outState.putInt(
            KEY_ITEM_INDEX,
            startItemIndex,
        )
        outState.putLong(
            KEY_POSITION,
            startPosition,
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

    fun onClickMenuOrBack(v: View?) {
        finish()
    }
    override fun callAct(str: Float, onlyfile: Boolean, dirFile: Boolean) {
        // 设置播放速度和音调均为2倍速
        player?.playbackParameters = PlaybackParameters(str, 1F)
        if (onlyfile)player?.repeatMode = REPEAT_MODE_ONE

        if (dirFile)player?.repeatMode = REPEAT_MODE_ALL

        // player?.play()
    }

    // 设置是滞全屏
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
