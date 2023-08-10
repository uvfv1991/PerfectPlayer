package com.example.perfectplayer.ui.album.SettingFragment // ktlint-disable package-name

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.perfectplayer.R
import com.example.perfectplayer.manager.IFragmentCallback
import com.example.perfectplayer.utils.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_base_pager.cb_dir
import kotlinx.android.synthetic.main.fragment_base_pager.cb_simple
import kotlinx.android.synthetic.main.fragment_base_pager.cb_slider
import kotlinx.android.synthetic.main.fragment_base_pager.option1
import kotlinx.android.synthetic.main.fragment_base_pager.option2
import kotlinx.android.synthetic.main.fragment_base_pager.option4
import kotlinx.android.synthetic.main.fragment_base_pager.slider
import kotlinx.android.synthetic.main.fragment_base_pager.tv_bsbf
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

/**
 *  author : jiangxue
 *  date : 2023/8/7 9:34
 *  description :
 */
class SettingBottomSheetFragment : BottomSheetDialogFragment(), View.OnTouchListener {

    private var mContext: Context? = null
    private var view: View? = null
    val list: MutableList<Fragment> = ArrayList()
    val name: MutableList<String> = ArrayList()
    private lateinit var mCallback: IFragmentCallback

    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        Log.e("TAG", "onCreateDialog: ")
        // 返回BottomSheetDialog的实例
        return BottomSheetDialog(this.requireContext())
    }
    private fun fixHeight() {
        if (null == getView()) {
            return
        }
        val parent = getView()!!.parent as View
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(parent)
        getView()!!.measure(0, 0)
        behavior.peekHeight = getView()!!.measuredHeight
        val params = parent.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        parent.layoutParams = params
    }
    override fun getView(): View? {
        return super.getView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isAdded) {
            mCallback = context as IFragmentCallback
        }
    }

    override fun onStart() {
        Log.e("TAG", "onStart: ")
        super.onStart()
        fixHeight()
        initRadioGroup()
        initEvent()
    }
    var simpleFile = false
    var allFile = false
    private fun initEvent() {
        cb_slider.isChecked = true
        slider.value = 1.0F
        slider.addOnChangeListener { slider, value, fromUser ->
            tv_bsbf.text = "变速播放：" + ScreenUtils.getFloatNoMoreThanOneDigits(value).toString()
        }
        cb_slider.onCheckedChange { buttonView, isChecked -> if (isChecked)slider.value = 1.0F }
        slider?.setOnTouchListener(this)
        cb_simple.onCheckedChange { buttonView, isChecked ->
            if (isChecked) simpleFile = true
            mCallback.callAct(slider.value, true, cb_dir.isChecked)
        }
        cb_dir.onCheckedChange { buttonView, isChecked ->
            if (isChecked)allFile = true
            mCallback.callAct(slider.value, cb_simple.isChecked, true)
        }

        option1.onCheckedChange { buttonView, isChecked -> }
    }

    private fun initRadioGroup() {
        // 为了能够在一行显示两个radiobutton:获取屏幕的宽度
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val point = Point()
        wm.getDefaultDisplay().getSize(point)

        val width = point.x
        val height: Int = dpToPx(requireContext(), 30)

        val params2 = option2.getLayoutParams() as LinearLayout.LayoutParams
        params2.setMargins(width / 2, -height, 0, 0)
        option2.setLayoutParams(params2)

        val params4 = option4.getLayoutParams() as LinearLayout.LayoutParams
        params4.setMargins(width / 2, -height, 0, 0) // 设置view和父容器之间间矩
        option4.setLayoutParams(params4)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        mContext = context
        view = inflater.inflate(R.layout.fragment_base_pager, container, false)

        return view
    }

    companion object {
        val instance: SettingBottomSheetFragment
            get() = SettingBottomSheetFragment()
    }

    // 该方法用于dp To px
    public fun dpToPx(context: Context, dp: Int): Int {
        val density: Float
        density = context.resources
            .displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        val action = p1?.getAction()

        if (action == MotionEvent.ACTION_UP) {
            mCallback.callAct(slider.value, simpleFile, allFile)
        }

        // 返回false表示不消费该事件，继续传递给其他监听器处理
        return false
    }
}
