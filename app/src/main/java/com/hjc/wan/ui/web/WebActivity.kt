package com.hjc.wan.ui.web

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.web.contract.WebContract
import com.hjc.wan.ui.web.presenter.WebPresenter
import com.hjc.wan.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_web.*

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:47
 * @Description: 含有WebView的Activity基类
 */
@Route(path = RoutePath.URL_WEB)
class WebActivity : BaseMvpActivity<WebContract.View, WebPresenter>(), WebContract.View {
    @JvmField
    @Autowired(name = "title")
    var mTitle: String = ""

    @JvmField
    @Autowired(name = "url")
    var mUrl: String = ""

    override fun createPresenter(): WebPresenter {
        return WebPresenter()
    }

    override fun createView(): WebContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    public override fun initView() {
        super.initView()

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.mipmap.icon_more)
        tvTitle.isSelected = true
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        if (!StringUtils.isEmpty(mTitle) && !StringUtils.isEmpty(mUrl)) {
            tvTitle.text = mTitle
            webView.loadUrl(mUrl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            // 刷新页面
            R.id.item_refresh -> if (webView != null) {
                webView.reload()
            }

            // 添加到收藏
            R.id.item_collect -> {
                getPresenter().collectLink(mTitle, mUrl)
            }

            // 分享
            R.id.item_share -> {
                CommonUtils.share(this, mUrl)
            }

            // 复制链接
            R.id.item_copy -> {
                CommonUtils.copy(this, mUrl)
                ToastUtils.showShort("复制成功")
            }

            // 打开链接
            R.id.item_open -> {
                CommonUtils.openLink(this, mUrl)
            }


            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

}
