package com.hjc.baselib.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.R
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseFragment
import com.hjc.baselib.utils.ClickUtils
import com.hjc.baselib.utils.helper.ActivityManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:45
 * @Description: FragmentActivity基类
 */
abstract class BaseFragmentActivity : RxAppCompatActivity(), View.OnClickListener {

    private lateinit var mBinder: Unbinder

    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initView()
        initData(savedInstanceState)
        addListeners()
    }


    /**
     * 获取布局的ID
     */
    abstract fun getLayoutId(): Int

    /**
     * 布局中Fragment的容器ID
     */
    abstract fun getFragmentContentId(): Int


    /**
     * 初始化View
     */
    protected open fun initView() {
        mBinder = ButterKnife.bind(this)
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
    }

    /**
     * 是否使用沉浸式
     */
    protected open fun isImmersionBarEnabled(): Boolean {
        return true
    }

    /**
     * 初始化沉浸式
     */
    protected open fun initImmersionBar() { //使用该属性,必须指定状态栏颜色
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorPrimary)
            .fitsSystemWindows(true)
            .init()
    }


    /**
     * 初始化数据
     */
    protected open fun initData(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        ActivityManager.addActivity(this)
        EventManager.register(this)
    }


    /**
     * EventBus接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun receiveEvent(event: MessageEvent<*>?) {
    }

    /**
     * EventBus处理
     */
    protected open fun handleMessage(event: MessageEvent<*>?) {

    }


    /**
     * 设置监听器
     */
    protected open fun addListeners(){}

    override fun onClick(v: View?) {
        //避免快速点击
        if (ClickUtils.isFastClick()) {
            ToastUtils.showShort("点的太快了,歇会呗!")
            return
        }
        onSingleClick(v)
    }

    /**
     * 设置点击事件
     */
    protected open fun onSingleClick(v: View?){}


    /**
     * 显示fragment
     */
    protected fun showFragment(fragment: BaseFragment) {
        if (mCurrentFragment !== fragment) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.hide(mCurrentFragment)
            mCurrentFragment = fragment
            if (!fragment.isAdded) {
                ft.add(getFragmentContentId(), fragment).show(fragment).commit()
            } else {
                ft.show(fragment).commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinder.unbind()
        EventManager.unregister(this)
    }
}