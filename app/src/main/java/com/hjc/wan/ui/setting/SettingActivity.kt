package com.hjc.wan.ui.setting

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.setting.contract.SettingContract
import com.hjc.wan.ui.setting.presenter.SettingPresenter
import kotlinx.android.synthetic.main.activity_setting.*

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


    override fun addListeners() {
        super.addListeners()

        llClearCache.setOnClickListener(this)
        llAnimation.setOnClickListener(this)
        rlTheme.setOnClickListener(this)
        llVersion.setOnClickListener(this)
        llProject.setOnClickListener(this)
        btnLogout.setOnClickListener(this)

        titleBar.setOnViewLeftClickListener { finish() }
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)

        when (v?.id) {
            R.id.llClearCache -> {
                ToastUtils.showShort("清除缓存")
            }
            R.id.llAnimation -> {
                ToastUtils.showShort("列表动画")
            }
            R.id.rlTheme -> {
                ToastUtils.showShort("主题颜色")
            }
            R.id.llVersion -> {
                ToastUtils.showShort("当前版本")
            }
            R.id.llProject -> {
                ToastUtils.showShort("项目源码")
            }
            R.id.btnLogout -> {
                ToastUtils.showShort("退出登录")
            }
        }
    }


    override fun changeListAnimation() {

    }

    override fun changeTheme() {

    }

}