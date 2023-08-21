package com.example.perfectplayer

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.BusUtils
import com.example.perfectplayer.base.BaseActivity
import com.example.perfectplayer.common.Constant
import com.example.perfectplayer.event.MessageEvent
import com.example.perfectplayer.ui.album.AlbumFragment
import com.permissionx.guolindev.PermissionX
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private var lastTime: Long = 0
    private var tag: String = "album"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.container, AlbumFragment(), tag).commit()
    }

    override fun onStart() {
        super.onStart()
        initPermissionX()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed()
            finish()
        } else {
            lastTime = currentTime
        }
    }

    public fun doubleClickExit() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed()
            finish()
        } else {
            lastTime = currentTime
            toast("双击后可以退出")
        }
    }

    private fun initPermissionX() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
            )
            .onExplainRequestReason { scope, deniedList ->
                val message = "PerfectPlayer需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doCancelBottom() {
    }

    @BusUtils.Bus(tag = Constant.one)
    public fun noParamFun(param: Int) {
    }

    @Subscribe
    fun onEvent(event: MessageEvent) {
        when (event.getType()) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        val ra = fragment as AlbumFragment

        return ra.onKeyDown(keyCode, event!!)
    }
}
