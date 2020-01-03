package com.hjc.baselib.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import butterknife.ButterKnife
import butterknife.Unbinder
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.R
import com.hjc.baselib.base.IBaseView
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.utils.ClickUtils
import com.hjc.baselib.widget.StatusView
import com.hjc.baselib.widget.bar.TitleBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2020/1/3 11:49
 * @Description: Fragment基类
 */
abstract class BaseFragment : RxFragment(), View.OnClickListener, IBaseView {

    protected lateinit var titleBar: TitleBar
    protected lateinit var smartRefreshLayout: SmartRefreshLayout
    private lateinit var statusView: StatusView

    private lateinit var mBinder: Unbinder

    /**
     * Fragment对应的Activity(避免使用getActivity()导致空指针异常)
     */
    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_base, container, false)
        val contentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null, false)

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentView.layoutParams = params

        titleBar = rootView.findViewById(R.id.title_bar)
        smartRefreshLayout = rootView.findViewById(R.id.smart_refresh_layout)
        statusView = rootView.findViewById(R.id.status_view)

        statusView.addView(contentView)

        mBinder = ButterKnife.bind(this, rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData(savedInstanceState)
        addListeners()
    }


    /**
     * 获取布局的ID
     */
    abstract fun getLayoutId(): Int


    /**
     * 初始化View
     */
    protected open fun initView() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
        initTitleBar()
        initSmartRefreshLayout()
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
     * 初始化标题
     */
    protected open fun initTitleBar() {}

    /**
     * 初始化刷新
     */
    protected open fun initSmartRefreshLayout() {
        smartRefreshLayout.setEnableRefresh(false)
        smartRefreshLayout.setEnableLoadMore(false)
    }


    /**
     * 初始化数据
     */
    protected open fun initData(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
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
    protected open fun handleMessage(event: MessageEvent<*>?) {}


    /**
     * 设置监听器
     */
    protected open fun addListeners(){}

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
    protected open fun onSingleClick(v: View?){}

    override fun showLoading() {
        statusView.showLoading()
    }

    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                statusView.showContent()
                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.setEnableLoadMore(true)
            }
    }

    override fun showEmpty() {
        statusView.showEmpty()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showError() {
        statusView.showError()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun showNoNetwork() {
        statusView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.setEnableLoadMore(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinder.unbind()
        EventManager.unregister(this)
    }
}
