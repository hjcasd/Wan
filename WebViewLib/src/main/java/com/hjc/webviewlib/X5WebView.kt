package com.hjc.webviewlib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * @Author: HJC
 * @Date: 2019/7/18 10:42
 * @Description: 自定义X5WebView
 */
class X5WebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {

    private  var  mProgressBar: ProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)

    init {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 2f))
        mProgressBar.layoutParams = layoutParams

        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_web_progress_bar)
        mProgressBar.progressDrawable = drawable
        addView(mProgressBar)

        initWebViewSettings()
        initClient()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSettings() {
        val webSetting = this.settings

        // 启用JavaScript执行。默认的是false。
        webSetting.javaScriptEnabled = true

        // 支持通过JS打开新窗口
        webSetting.javaScriptCanOpenWindowsAutomatically = true

        // 在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等
        webSetting.allowFileAccess = true

        // 排版适应屏幕
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS

        // 是否应该支持使用其屏幕缩放控件和手势缩放
        webSetting.setSupportZoom(true)

        // 进行控制缩放
        webSetting.builtInZoomControls = true

        // 设置此属性，可任意比例缩放
        webSetting.useWideViewPort = true

        // 启动应用缓存
        webSetting.setAppCacheEnabled(true)
        // 设置应用缓存内容的最大值
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // 设置缓存模式
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE

        // DOM存储API是否可用
        webSetting.domStorageEnabled = true

        // 定位是否可用
        webSetting.setGeolocationEnabled(true)

        //设置是否支持插件
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置安全的来源
            webSetting.mixedContentMode = webSetting.mixedContentMode
        }

        // 不缩放
        setInitialScale(100)
    }

    private fun initClient() {
        webViewClient = object : WebViewClient() {
            // 防止加载网页时调起系统浏览器
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, newProgress: Int) {
                mProgressBar.progress = newProgress
                if (newProgress == 100) {
                    mProgressBar.visibility = GONE
                } else {
                    mProgressBar.visibility = VISIBLE
                }
                super.onProgressChanged(webView, newProgress)
            }
        }
    }

    /*
     * 将dp转换为与之相等的px
     */
    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}