package com.hjc.wan.ui.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ConvertUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.event.EventManager
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseFragment
import com.hjc.wan.adapter.MyViewPagerAdapter
import com.hjc.wan.constant.EventCode
import com.hjc.wan.databinding.FragmentIndicatorBinding
import com.hjc.wan.model.ClassifyBean
import com.hjc.wan.ui.project.child.ProjectChildFragment
import com.hjc.wan.ui.project.contract.ProjectContract
import com.hjc.wan.ui.project.presenter.ProjectPresenter
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.indicator.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 项目页面
 */
class ProjectFragment :
    BaseFragment<FragmentIndicatorBinding, ProjectContract.View, ProjectPresenter>(),
    ProjectContract.View {

    companion object {

        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }

    override fun createPresenter(): ProjectPresenter {
        return ProjectPresenter()
    }

    override fun createView(): ProjectContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        mBinding.magicIndicator.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initData(savedInstanceState: Bundle?) {
        EventManager.register(this)
        getPresenter()?.loadProjectTitles()
    }

    override fun addListeners() {

    }

    override fun onSingleClick(v: View?) {

    }

    override fun showIndicator(classifyList: MutableList<ClassifyBean>) {
        // ViewPager init
        val fragments = ArrayList<Fragment>()
        for (bean in classifyList) {
            fragments.add(ProjectChildFragment.newInstance(bean.id))
        }
        val adapter = MyViewPagerAdapter(childFragmentManager, fragments)
        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.offscreenPageLimit = fragments.size

        // Indicator init
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return classifyList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(classifyList[index].name)
                    textSize = 18f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener { mBinding.viewPager.setCurrentItem(index, false) }
                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    lineHeight = ConvertUtils.dp2px(3.0f).toFloat()
                    lineWidth = ConvertUtils.dp2px(30.0f).toFloat()
                    roundRadius = ConvertUtils.dp2px(6.0f).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }
        }
        mBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewPager)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.CHANGE_THEME) {
            SettingManager.getThemeColor().let {
                mBinding.magicIndicator.setBackgroundColor(it)

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
