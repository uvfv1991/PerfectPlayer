package com.example.kotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.base.NoViewModel
import com.example.perfectplayer.R
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.databinding.ActivityVideoDetailBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.android.synthetic.main.activity_video_detail.styledPlayerView
import java.util.Formatter
import java.util.Locale


class VideoDetailActivity : BaseActivity<NoViewModel, ActivityVideoDetailBinding>(), StyledPlayerView.ControllerVisibilityListener {
    protected final var TAG : String = "VideoDetailActivity";
    private var mSimpleExoPlayer: SimpleExoPlayer? = null
    private var isPlaying = false

    var player: SimpleExoPlayer? =null
    lateinit var video:Video


    override fun layoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        var intent = intent
        video = intent.getSerializableExtra("detail") as Video
        Log.e(TAG,"url"+video.path)
    }

    private fun initPlayer() {
        //1. 构建播放器实例
        player = SimpleExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri(video.path.toString())
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady =  true
        styledPlayerView.player = player

    }



    override fun initData() {
        initPlayer()
        initListener()
    }


    //播放器监听
    private fun initListener() {
    //1. 构建监听器
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when(playbackState){
                    Player.STATE_ENDED->{}//播放结束
                    Player.STATE_BUFFERING -> {}// 正在缓冲
                        Player.STATE_IDLE -> {}// 空闲状态
                            Player.STATE_READY -> {}// 可以被播放状态
                }
            }

            override fun onPlayerError(error: PlaybackException) {
            }

            override fun onRenderedFirstFrame() {

            }


        }
        player?.addListener(listener)



    }

    /**
     * Starts or stops playback. Also takes care of the Play/Pause button toggling
     * @param play True if playback should be started
     */
    private fun setPlayPause(play: Boolean) {
        mSimpleExoPlayer!!.playWhenReady = play
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
    private fun togglePlay() {
        if (isPlaying) {
            player!!.pause()
        } else {
            player!!.play()
        }
        isPlaying = !isPlaying
    }

    //        注意：在使用ExoPlayer时，需要在Activity中保持屏幕常亮以避免视频播放过程中屏幕自动关闭。
    override fun onResume() {
        super.onResume()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mSimpleExoPlayer?.stop()
    }

    override fun onStop() {
        super.onStop()
        mSimpleExoPlayer?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mSimpleExoPlayer!=null)mSimpleExoPlayer?.release()
    }

    override fun onVisibilityChanged(visibility: Int) {
        TODO("Not yet implemented")
    }

    fun onClickMenuOrBack(v: View?) {
        finish()

    }
}



