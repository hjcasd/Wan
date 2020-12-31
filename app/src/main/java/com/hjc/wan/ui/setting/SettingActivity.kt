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
import com.hjc.baselib.activity.BaseActivity
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.utils.helper.ActivityHelper
import com.hjc.baselib.widget.bar.OnBarLeftClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivitySettingBinding
import com.hjc.wan.ui.setting.contract.SettingContract
import com.hjc.wan.ui.setting.presenter.SettingPresenter
import com.hjc.wan.utils.helper.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @Author: HJC
 * @Date: 2019/12/25 14:07
 * @Description: 设置页面
 */
@Route(path = RoutePath.URL_SETTING)
class SettingActivity :
    BaseActivity<ActivitySettingBinding, SettingContract.View, SettingPresenter>(),
    SettingContract.View {

    override fun createPresenter(): SettingPresenter {
        return SettingPresenter()
    }

    override fun createView(): SettingContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        SettingManager.getThemeColor().let {
            mBinding.colorView.setViewColor(it)
            mBinding.titleBar.setBgColor(it)

            val drawable = mBinding.btnLogout.background as GradientDrawable
            drawable.setColor(it)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        EventManager.register(this)

        val totalCacheSize = CacheManager.getTotalCacheSize(this)
        mBinding.tvCache.text = totalCacheSize

        val type = SettingManager.getListAnimationType()
        mBinding.tvAnimation.text = resources.getStringArray(R.array.setting_modes)[type]
    }

    override fun addListeners() {
        mBinding.llClearCache.setOnClickListener(this)
        mBinding.llAnimation.setOnClickListener(this)
        mBinding.rlTheme.setOnClickListener(this)
        mBinding.llVersion.setOnClickListener(this)
        mBinding.llProject.setOnClickListener(this)
        mBinding.btnLogout.setOnClickListener(this)

        mBinding.titleBar.setOnBarLeftClickListener(object : OnBarLeftClickListener {

            override fun leftClick(view: View) {
                finish()
            }

        })
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.ll_clear_cache -> clearCache()

            R.id.ll_animation -> changeListAnimationType()

            R.id.rl_theme -> changeTheme()

            R.id.ll_version -> getPresenter().checkVersion()

            R.id.ll_project -> RouterManager.jumpToWeb("Wan", "https://github.com/hjcasd/Wan")

            R.id.btn_logout -> logout()

        }
    }

    /**
     * 清除缓存
     */
    @SuppressLint("SetTextI18n")
    private fun clearCache() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.app_tip)
            message(text = "确定清除缓存吗")
            positiveButton(text = "确定") {
                ToastUtils.showShort("清除缓存成功")
                CacheManager.clearTotalCache(this@SettingActivity)
                mBinding.tvCache.text = "0.0B"
            }
            negativeButton(R.string.app_cancel)
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
                mBinding.tvAnimation.text = text

                EventManager.sendEvent(MessageEvent(EventCode.CHANGE_LIST_ANIMATION, null))
            }
            title(text = "设置列表动画")
            positiveButton(R.string.app_confirm)
            negativeButton(R.string.app_cancel)
        }
    }

    /**
     * 主题颜色
     */
    private fun changeTheme() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.app_choose_theme_color)
            colorChooser(
                ColorHelper.ACCENT_COLORS,
                initialSelection = SettingManager.getThemeColor(),
                subColors = ColorHelper.PRIMARY_SUB_COLORS
            ) { _, color ->
                SettingManager.setThemeColor(color)
                EventManager.sendEvent(MessageEvent(EventCode.CHANGE_THEME, null))
            }
            positiveButton(R.string.app_confirm)
            negativeButton(R.string.app_cancel)
        }
    }

    private fun logout() {
        MaterialDialog(this).show {
            cornerRadius(10f)
            title(R.string.app_tip)
            message(text = "确定退出登录吗")
            positiveButton(text = "确定") {
                getPresenter().logout()
            }
            negativeButton(R.string.app_cancel)
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
                mBinding.colorView.setViewColor(it)
                mBinding.titleBar.setBgColor(it)
                val drawable = mBinding.btnLogout.background as GradientDrawable
                drawable.setColor(it)

                ImmersionBar.with(this)
                    .statusBarColor(ColorUtils.int2RgbString(it))
                    .init()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unregister(this)
    }

}