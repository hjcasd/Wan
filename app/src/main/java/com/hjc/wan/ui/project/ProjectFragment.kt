package com.hjc.wan.ui.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ConvertUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseMvpFragment
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.model.ClassifyBean
import com.hjc.wan.adapter.MyViewPagerAdapter
import com.hjc.wan.ui.project.child.ProjectChildFragment
import com.hjc.wan.ui.project.contract.ProjectContract
import com.hjc.wan.ui.project.presenter.ProjectPresenter
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.indicator.ScaleTransitionPagerTitleView
import kotlinx.android.synthetic.main.fragment_indicator.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.util.*


/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 项目页面
 */
class ProjectFragment : BaseMvpFragment<ProjectContract.View, ProjectPresenter>(),
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_indicator
    }

    override fun initView() {
        super.initView()

        magicIndicator.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
            .init()
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter()?.loadProjectTitles()
    }

    override fun showIndicator(classifyList: MutableList<ClassifyBean>) {
        // ViewPager init
        val fragments = ArrayList<Fragment>()
        for (bean in classifyList) {
            fragments.add(ProjectChildFragment.newInstance(bean.id))
        }
        val adapter = MyViewPagerAdapter(childFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size

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
                    setOnClickListener { viewPager.setCurrentItem(index, false) }
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
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewPager)
    }

    override fun handleMessage(event: MessageEvent<*>?) {
        if (event?.code == EventCode.CHANGE_THEME) {
            SettingManager.getThemeColor().let {
                magicIndicator.setBackgroundColor(it)

                ImmersionBar.with(this)
                    .statusBarColor(ColorUtils.int2RgbString(it))
                    .init()
            }
        }
    }
}
