package com.hjc.wan.ui.web

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityWebBinding
import com.hjc.wan.ui.web.contract.WebContract
import com.hjc.wan.ui.web.presenter.WebPresenter
import com.hjc.wan.utils.CommonUtils
import com.hjc.wan.utils.helper.SettingManager

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:47
 * @Description: 含有WebView的Activity基类
 */
@Route(path = RoutePath.URL_WEB)
class WebActivity : BaseActivity<ActivityWebBinding, WebContract.View, WebPresenter>(),
    WebContract.View {
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

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        setSupportActionBar(mBinding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        mBinding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.mipmap.icon_more)
        mBinding.tvTitle.isSelected = true

        mBinding.toolbar.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (!StringUtils.isEmpty(mTitle) && !StringUtils.isEmpty(mUrl)) {
            mBinding.tvTitle.text = mTitle
            mBinding.webView.loadUrl(mUrl)
        }
    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()

            // 关闭页面
            R.id.item_close -> finish()

            // 刷新页面
            R.id.item_refresh -> mBinding.webView.reload()

            // 添加到收藏
            R.id.item_collect -> getPresenter().collectLink(mTitle, mUrl)

            // 分享
            R.id.item_share -> CommonUtils.share(this, mUrl)

            // 复制链接
            R.id.item_copy -> {
                CommonUtils.copy(this, mUrl)
                ToastUtils.showShort("复制成功")
            }

            // 打开链接
            R.id.item_open -> CommonUtils.openLink(this, mUrl)

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack()
        } else {
            finish()
        }
    }

}
