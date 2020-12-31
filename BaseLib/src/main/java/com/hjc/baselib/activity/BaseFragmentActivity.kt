package com.hjc.baselib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.base.BasePresenter
import com.hjc.baselib.base.IBaseView
import com.hjc.baselib.utils.ClickUtils
import java.lang.reflect.ParameterizedType

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:47
 * @Description: FragmentActivity基类(MVP)
 */
abstract class BaseFragmentActivity<VB : ViewBinding, V : IBaseView, P : BasePresenter<V>?> : AppCompatActivity(), View.OnClickListener {

    protected lateinit var mBinding: VB

    private var mPresenter: P? = null

    private lateinit var mView: V

    private var mCurrentFragment = Fragment()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            mBinding = method.invoke(null, layoutInflater) as VB
            setContentView(mBinding.root)
        }

        initView()
        initData(savedInstanceState)
        addListeners()
    }

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
        ARouter.getInstance().inject(this)
        getImmersionBar()?.init()
        mPresenter = createPresenter()
        mView = createView()
        mPresenter?.attachView(mView)
    }

    abstract fun createPresenter(): P

    abstract fun createView(): V

    protected fun getPresenter(): P? {
        return mPresenter
    }

    /**
     * 初始化数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 设置监听器
     */
    abstract fun addListeners()

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
    abstract fun onSingleClick(v: View?)

    /**
     * 显示fragment
     */
    protected fun showFragment(fragment: Fragment) {
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

    /**
     * 布局中Fragment的容器ID
     */
    abstract fun getFragmentContentId(): Int

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}