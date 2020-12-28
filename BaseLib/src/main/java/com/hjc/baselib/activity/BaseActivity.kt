package com.hjc.baselib.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.base.IBaseView
import com.hjc.baselib.dialog.LoadingDialog
import com.hjc.baselib.utils.ClickUtils

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:45
 * @Description:  Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, IBaseView {

    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        ARouter.getInstance().inject(this)
        initView()
        initData(savedInstanceState)
        addListeners()
    }

    /**
     * 获取布局的ID
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化沉浸式
     */
    protected open fun getImmersionBar(): ImmersionBar? {
        return null
    }

    /**
     * 初始化View
     */
    protected open fun initView() {
        getImmersionBar()?.init()
    }

    /**
     * 初始化数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 设置监听器
     */
    abstract fun addListeners()

    override fun onClick(v: View?) { //避免快速点击
        if (ClickUtils.isFastClick()) {
            ToastUtils.showShort("点的太快了,歇会呗!")
            return
        }
        onSingleClick(v)
    }

    /**
     * 设置点击事件
     */
    abstract fun onSingleClick(v: View?)

    override fun startLoading() {
        mLoadingDialog = LoadingDialog.newInstance()
        mLoadingDialog?.showDialog(supportFragmentManager)
    }

    override fun dismissLoading() {
        mLoadingDialog?.let {
            val dialog = it.dialog
            if (dialog != null && dialog.isShowing) {
                it.dismiss()
            }
        }
    }
}