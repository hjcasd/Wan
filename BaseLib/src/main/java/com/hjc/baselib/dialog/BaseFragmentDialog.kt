package com.hjc.baselib.dialog

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.utils.ClickUtils

abstract class BaseFragmentDialog : DialogFragment(), View.OnClickListener {

    protected lateinit var mContext: Context

    /**
     * dialog显示位置
     */
    private var mGravity = Gravity.CENTER

    /**
     * dialog进入退出动画
     */
    private var mAnimStyle = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, getStyleRes())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
        addListeners()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let { self ->
            val window = self.window
            window?.let {
                val params = it.attributes
                params.gravity = mGravity
                params.width = getWidth()
                params.height = getHeight()
                //设置dialog动画
                if (mAnimStyle != 0) {
                    it.setWindowAnimations(mAnimStyle)
                }
                it.attributes = params
            }
        }
    }

    /**
     * 设置宽度为屏幕宽度的0.8
     */
    protected open fun getWidth(): Int {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        return (width * 0.8).toInt()
    }

    /**
     * 设置高度wrap
     */
    protected open fun getHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    /**
     * 设置Dialog位置
     */
    fun setGravity(gravity: Int): BaseFragmentDialog {
        mGravity = gravity
        return this
    }

    /**
     * 设置动画类型
     */
    fun setAnimStyle(@StyleRes animStyle: Int): BaseFragmentDialog {
        mAnimStyle = animStyle
        return this
    }

    /**
     * 显示Fragment
     */
    fun showDialog(fm: FragmentManager) {
        show(fm, "DialogFragment")
    }

    /**
     * 获取Dialog的Theme
     */
    abstract fun getStyleRes(): Int

    /**
     * 获取布局的ID
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 设置监听器
     */
    abstract fun addListeners()

    /**
     * 设置点击事件
     */
    abstract fun onSingleClick(v: View?)

    override fun onClick(view: View) { //避免快速点击
        if (ClickUtils.isFastClick()) {
            ToastUtils.showShort("点的太快了,歇会呗!")
            return
        }
        onSingleClick(view)
    }
}