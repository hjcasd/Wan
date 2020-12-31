package com.hjc.baselib.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.base.BasePresenter
import com.hjc.baselib.base.IBaseView
import com.hjc.baselib.dialog.LoadingDialog
import com.hjc.baselib.loadsir.EmptyCallback
import com.hjc.baselib.loadsir.ErrorCallback
import com.hjc.baselib.loadsir.LoadingCallback
import com.hjc.baselib.utils.ClickUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:50
 * @Description: Fragment基类(MVP)
 */
abstract class BaseFragment<VB : ViewBinding, V : IBaseView, P : BasePresenter<V>?> : Fragment(), View.OnClickListener, IBaseView {

    protected lateinit var mBinding: VB

    private var mPresenter: P? = null

    private lateinit var mView: V

    protected var mLoadService: LoadService<*>? = null

    protected lateinit var mContext: Context

    private var mLoadingDialog: LoadingDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        mBinding = method.invoke(null, layoutInflater, container, false) as VB
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData(savedInstanceState)
        addListeners()
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

    /**
     * 初始化沉浸式
     */
    protected open fun getImmersionBar(): ImmersionBar? {
        return null
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

    /**
     * 注册LoadSir
     *
     * @param view 状态视图
     */
    protected open fun initLoadSir(view: View?) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault().register(view) { v: View? -> this.onRetryBtnClick(v) }
        }
    }

    override fun startLoading() {
        mLoadingDialog = LoadingDialog.newInstance()
        mLoadingDialog?.showDialog(childFragmentManager)
    }

    override fun dismissLoading() {
        mLoadingDialog?.let {
            val dialog = it.dialog
            if (dialog != null && dialog.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun showContent() {
        GlobalScope.launch {
            delay(500)
            mLoadService?.showSuccess()
        }
    }

    override fun showLoading() {
        mLoadService?.showCallback(LoadingCallback::class.java)
    }

    override fun showEmpty() {
        GlobalScope.launch {
            delay(500)
            mLoadService?.showCallback(EmptyCallback::class.java)
        }
    }

    override fun showError() {
        GlobalScope.launch {
            delay(500)
            mLoadService?.showCallback(ErrorCallback::class.java)
        }
    }

    /**
     * 失败重试,重新加载事件
     */
    protected open fun onRetryBtnClick(v: View?) {}

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getImmersionBar()?.init()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}
