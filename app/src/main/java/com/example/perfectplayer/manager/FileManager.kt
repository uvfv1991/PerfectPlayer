package com.example.perfectplayer.manager

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.FileUtils
import com.example.perfectplayer.PerfectApplication
import com.example.perfectplayer.data.Video
import com.example.perfectplayer.ui.album.AlbumFragment
import java.io.File
import java.util.stream.Collectors

/**
 *  author : jiangxue
 *  date : 2023/8/2 13:15
 *  description :
 */
class FileManager {
    private var mInstance: FileManager? = null
    private var mContext: Context? = null
    var mContentResolver: ContentResolver? = null
    private val mLock = Any()

    fun getInstance(context: Context): FileManager? {
        if (mInstance == null) {
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = FileManager()
                    mContext = context
                    mContentResolver = context.contentResolver
                }
            }
        }
        return mInstance
    }

    /**
     * 获取本机视频列表
     * @return
     */
    @SuppressLint("Range")
    public fun getVideos(): ArrayList<Video>? {
        val videos: ArrayList<Video> = ArrayList<Video>()
        var c: Cursor? = null
        try {
            c = PerfectApplication.context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER,
            )
            while (c?.moveToNext()!!) {
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)) // 路径
                val folder = File(path).getParentFile().getName()
                if (!FileUtils.isFileExists(path)) {
                    continue
                }
                val id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)) // 视频的id
                val name =
                    c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)) // 视频名称
                val resolution =
                    c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)) // 分辨率
                val size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)) // 大小
                val duration =
                    c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)) // 时长

                val tp =
                    c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)) // 时长
                val date =
                    c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)) // 修改时间

                val data =
                    c.getString(c.getColumnIndex(MediaStore.Files.FileColumns.DATA)) // 修改时间
                // count显示的是第个文件夹下视频数量
                val video = Video(
                    path, false, size, tp, false, folder, name, 0,
                    AlbumFragment.TYPE_LIST,
                )

                videos.add(video)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }
        return videos
    }

    /**
     * 获取本机视频列表
     * @return
     */
    public fun getVideoFolder(): ArrayList<Video> {
        val videos: ArrayList<Video> = ArrayList<Video>()
        var c: Cursor? = null
        try {
            c = PerfectApplication.context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER,
            )
            while (c?.moveToNext()!!) {
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)) // 路径
                val folder = File(path).getParentFile().getName()
                // 暂时只支持了mp4格式
                var count = File(path).getParentFile().walk()
                    .maxDepth(1)
                    .filter { it.isFile }
                    .filter { it.extension in listOf("mp4") }
                    .toList().size

                if (!FileUtils.isFileExists(path)) {
                    continue
                }
                // count显示的是第个文件夹下视频数量
                val video = Video(
                    null, false, 0, null, false, folder, null, count,
                    AlbumFragment.TYPE_NO,
                )

                videos.add(video)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }
        return videos
    }

    /**
     * 去重
     *
     * @param testList
     */
    @RequiresApi(Build.VERSION_CODES.N)
    public fun useSetDistinct(videoList: ArrayList<Video>): ArrayList<Video> {
        var result = videoList.stream().distinct().collect(Collectors.toList())
        return result as ArrayList<Video>
    }

    // 获取视频缩略图
    private fun getVideoThumbnail(id: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inDither = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(
            mContentResolver,
            id.toLong(),
            MediaStore.Images.Thumbnails.MICRO_KIND,
            options,
        )
        return bitmap
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var manager: FileManager? = null

        fun getInstance() = manager ?: synchronized(this) {
            manager ?: FileManager().also { manager = it }
        }
    }
}
