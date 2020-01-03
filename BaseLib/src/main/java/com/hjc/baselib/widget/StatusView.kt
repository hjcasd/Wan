package com.hjc.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.hjc.baselib.R
import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/4/26 11:56
 * @Description: 页面多状态视图
 */
class StatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null

    private val mEmptyViewResId: Int
    private val mErrorViewResId: Int
    private val mLoadingViewResId: Int
    private val mNoNetworkViewResId: Int
    private val mContentViewResId: Int

    /**
     * 获取当前状态
     *
     * @return 视图状态
     */
    private var mViewStatus = -1
    private val mInflater: LayoutInflater
    private var mOnRetryClickListener: OnClickListener? = null
    private var mViewStatusListener: OnViewStatusChangeListener? = null

    private val mOtherIds = ArrayList<Int>()

    companion object {
        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        const val STATUS_CONTENT = 0x00
        const val STATUS_LOADING = 0x01
        const val STATUS_EMPTY = 0x02
        const val STATUS_ERROR = 0x03
        const val STATUS_NO_NETWORK = 0x04

        private const val NULL_RESOURCE_ID = -1
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, 0)
        mEmptyViewResId = a.getResourceId(R.styleable.StatusView_emptyView, R.layout.default_empty_view)
        mErrorViewResId = a.getResourceId(R.styleable.StatusView_errorView, R.layout.default_error_view)
        mLoadingViewResId = a.getResourceId(R.styleable.StatusView_loadingView, R.layout.default_loading_view)
        mNoNetworkViewResId = a.getResourceId(R.styleable.StatusView_noNetworkView, R.layout.default_no_network_view)
        mContentViewResId = a.getResourceId(R.styleable.StatusView_contentView, NULL_RESOURCE_ID)
        a.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView)
        if (!mOtherIds.isEmpty()) {
            mOtherIds.clear()
        }
        if (null != mOnRetryClickListener) {
            mOnRetryClickListener = null
        }
        if (null != mViewStatusListener) {
            mViewStatusListener = null
        }
    }

    /**
     * 获取当前状态
     *
     * @return 视图状态
     */
    fun getViewStatus(): Int {
        return mViewStatus
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: OnClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showEmpty(layoutId: Int = mEmptyViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showEmpty(if (null == mEmptyView) inflateView(layoutId) else mEmptyView, layoutParams)
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showEmpty(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Empty view is null.")
        checkNull(layoutParams, "Layout params is null.")
        changeViewStatus(STATUS_EMPTY)
        if (null == mEmptyView) {
            mEmptyView = view
            val emptyRetryView = mEmptyView!!.findViewById<View>(R.id.empty_retry_view)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView!!.id)
            addView(mEmptyView, 0, layoutParams)
        }
        showViewById(mEmptyView!!.id)
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showError(layoutId: Int = mErrorViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showError(if (null == mErrorView) inflateView(layoutId) else mErrorView, layoutParams)
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Error view is null.")
        checkNull(layoutParams, "Layout params is null.")
        changeViewStatus(STATUS_ERROR)
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView!!.findViewById<View>(R.id.error_retry_view)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView!!.id)
            addView(mErrorView, 0, layoutParams)
        }
        showViewById(mErrorView!!.id)
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showLoading(layoutId: Int = mLoadingViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showLoading(if (null == mLoadingView) inflateView(layoutId) else mLoadingView, layoutParams)
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Loading view is null.")
        checkNull(layoutParams, "Layout params is null.")
        changeViewStatus(STATUS_LOADING)
        if (null == mLoadingView) {
            mLoadingView = view
            mOtherIds.add(mLoadingView!!.id)
            addView(mLoadingView, 0, layoutParams)
        }
        showViewById(mLoadingView!!.id)
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showNoNetwork(
        layoutId: Int = mNoNetworkViewResId,
        layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS
    ) {
        showNoNetwork(if (null == mNoNetworkView) inflateView(layoutId) else mNoNetworkView, layoutParams)
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "No network view is null.")
        checkNull(layoutParams, "Layout params is null.")
        changeViewStatus(STATUS_NO_NETWORK)
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView!!.findViewById<View>(R.id.no_network_retry_view)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView!!.id)
            addView(mNoNetworkView, 0, layoutParams)
        }
        showViewById(mNoNetworkView!!.id)
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        changeViewStatus(STATUS_CONTENT)
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    /**
     * 显示内容视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showContent(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showContent(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示内容视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showContent(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Content view is null.")
        checkNull(layoutParams, "Layout params is null.")
        changeViewStatus(STATUS_CONTENT)
        clear(mContentView)
        mContentView = view
        addView(mContentView, 0, layoutParams)
        showViewById(mContentView!!.id)
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater.inflate(layoutId, null)
    }

    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) View.VISIBLE else View.GONE
        }
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.GONE else View.VISIBLE
        }
    }

    private fun checkNull(`object`: Any?, hint: String) {
        if (null == `object`) {
            throw NullPointerException(hint)
        }
    }

    private fun clear(vararg views: View?) {
        if (null == views) {
            return
        }
        try {
            for (view in views) {
                if (null != view) {
                    removeView(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 视图状态改变接口
     */
    interface OnViewStatusChangeListener {

        /**
         * 视图状态改变时回调
         *
         * @param oldViewStatus 之前的视图状态
         * @param newViewStatus 新的视图状态
         */
        fun onChange(oldViewStatus: Int, newViewStatus: Int)
    }

    /**
     * 设置视图状态改变监听事件
     *
     * @param onViewStatusChangeListener 视图状态改变监听事件
     */
    fun setOnViewStatusChangeListener(onViewStatusChangeListener: OnViewStatusChangeListener) {
        this.mViewStatusListener = onViewStatusChangeListener
    }

    /**
     * 改变视图状态
     *
     * @param newViewStatus 新的视图状态
     */
    private fun changeViewStatus(newViewStatus: Int) {
        if (mViewStatus == newViewStatus) {
            return
        }
        if (null != mViewStatusListener) {
            mViewStatusListener!!.onChange(mViewStatus, newViewStatus)
        }
        mViewStatus = newViewStatus
    }

}
