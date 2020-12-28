package com.hjc.wan.ui.setting

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseMvpActivity
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.utils.helper.ActivityHelper
import com.hjc.baselib.widget.bar.OnBarLeftClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.setting.contract.SettingContract
import com.hjc.wan.ui.setting.presenter.SettingPresenter
import com.hjc.wan.utils.helper.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @Author: HJC
 * @Date: 2019/12/25 14:07
 * @Description: 设置页面
 */
@Route(path = RoutePath.URL_SETTING)
class SettingActivity : BaseMvpActivity<SettingContract.View, SettingPresenter>(),
    SettingContract.View {

    override fun createPresenter(): SettingPresenter {
        return SettingPresenter()
    }

    override fun createView(): SettingContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        SettingManager.getThemeColor().let {
            colorView.setViewColor(it)

            val drawable = btnLogout.background as GradientDrawable
            drawable.setColor(it)
        }

        titleBar.setBgColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        val totalCacheSize = CacheManager.getTotalCacheSize(this)
        tvCache.text = totalCacheSize

        val type = SettingManager.getListAnimationType()
        tvAnimation.text = resources.getStringArray(R.array.setting_modes)[type]
    }

    override fun addListeners() {
        llClearCache.setOnClickListener(this)
        llAnimation.setOnClickListener(this)
        rlTheme.setOnClickListener(this)
        llVersion.setOnClickListener(this)
        llProject.setOnClickListener(this)
        btnLogout.setOnClickListener(this)

        titleBar.setOnBarLeftClickListener(object : OnBarLeftClickListener {

            override fun leftClick(view: View) {
                finish()
            }

        })
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.llClearCache -> {
                clearCache()
            }
            R.id.llAnimation -> {
                changeListAnimationType()
            }
            R.id.rlTheme -> {
                changeTheme()
            }
            R.id.llVersion -> {
                getPresenter().checkVersion()
            }
            R.id.llProject -> {
                RouterManager.jumpToWeb("Wan", "https://github.com/hjcasd/Wan")
            }
            R.id.btnLogout -> {
                logout()
            }
        }
    }

    /**
     * 清除缓存
     */
    @SuppressLint("SetTextI18n")
    private fun clearCache() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.title)
            message(text = "确定清除缓存吗")
            positiveButton(text = "确定") {
                ToastUtils.showShort("清除缓存成功")
                CacheManager.clearTotalCache(this@SettingActivity)
                this@SettingActivity.tvCache.text = "0.0B"
            }
            negativeButton(R.string.cancel)
        }
    }

    /**
     * 列表动画
     */
    private fun changeListAnimationType() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            listItemsSingleChoice(
                R.array.setting_modes,
                initialSelection = SettingManager.getListAnimationType()
            ) { dialog, index, text ->
                SettingManager.setListAnimationType(index)
                this@SettingActivity.tvAnimation.text = text

                EventManager.sendEvent(MessageEvent(EventCode.CHANGE_LIST_ANIMATION, null))
            }
            title(text = "设置列表动画")
            positiveButton(R.string.confirm)
            negativeButton(R.string.cancel)
        }
    }

    /**
     * 主题颜色
     */
    private fun changeTheme() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.choose_theme_color)
            colorChooser(
                ColorHelper.ACCENT_COLORS,
                initialSelection = SettingManager.getThemeColor(),
                subColors = ColorHelper.PRIMARY_SUB_COLORS
            ) { _, color ->
                SettingManager.setThemeColor(color)
                EventManager.sendEvent(MessageEvent(EventCode.CHANGE_THEME, null))
            }
            positiveButton(R.string.confirm)
            negativeButton(R.string.cancel)
        }
    }

    private fun logout() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.title)
            message(text = "确定退出登录吗")
            positiveButton(text = "确定") {
                getPresenter().logout()
            }
            negativeButton(R.string.cancel)
        }
    }

    override fun toLogin() {
        AccountManager.clear()
        ActivityHelper.finishAllActivities()
        RouterManager.jump(RoutePath.URL_LOGIN)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.CHANGE_THEME) {
            SettingManager.getThemeColor().let {
                colorView.setViewColor(it)
                titleBar.setBgColor(it)
                val drawable = btnLogout.background as GradientDrawable
                drawable.setColor(it)

                ImmersionBar.with(this)
                    .statusBarColor(ColorUtils.int2RgbString(it))
                    .init()
            }
        }
    }

}