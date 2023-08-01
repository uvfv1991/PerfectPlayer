package com.example.perfectplayer

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.BusUtils.Bus
import com.example.perfectplayer.base.BaseActivity
import com.example.perfectplayer.common.Constant
import com.example.perfectplayer.manager.IOneFragmentCallback
import com.example.perfectplayer.ui.album.AlbumFragment
import com.example.perfectplayer.ui.gallery.GalleryFragment
import com.example.perfectplayer.ui.slideshow.SlideshowFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.permissionx.guolindev.PermissionX
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(){
    private var type = 0
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private var lastTime: Long = 0

    private var albumFragment: AlbumFragment? = null
    private var galleryFragment: GalleryFragment? = null
    private var slideshowFragment: SlideshowFragment? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private val mFragments = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24, null)
        setSupportActionBar(toolbar)
        initView()
    }

    private fun initView() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
        }

    }

    override fun onStart() {
        super.onStart()
        BusUtils.register(this);
        initPermissionX()
    }

    override fun onStop() {
        super.onStop()
        BusUtils.unregister(this);
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

    private fun doubleClickExit() {
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
                Manifest.permission.RECORD_AUDIO
            )
            //.setDialogTintColor(Color.parseColor("#008577"), Color.parseColor("#83e8dd"))
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //根据状态判断


            }
            R.id.menurefresh -> {
                toast("刷新一下")
            }
            R.id.menuhistory -> {
                toast("历史记录")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @BusUtils.Bus(tag = Constant.one )
     public fun noParamFun(param:Int) {
        type=param
        toast("noParamFun")
    }


}
