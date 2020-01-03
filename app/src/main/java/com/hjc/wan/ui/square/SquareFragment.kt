package com.hjc.wan.ui.square

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.hjc.baselib.event.MessageEvent
import com.hjc.baselib.fragment.BaseMvpFragment
import com.hjc.wan.R
import com.hjc.wan.constant.EventCode
import com.hjc.wan.ui.adapter.MyViewPagerAdapter
import com.hjc.wan.ui.square.child.NavigationFragment
import com.hjc.wan.ui.square.child.PlazaFragment
import com.hjc.wan.ui.square.child.SystemFragment
import com.hjc.wan.ui.square.contract.SquareContract
import com.hjc.wan.ui.square.presenter.SquarePresenter
import com.hjc.wan.utils.helper.SettingManager
import com.hjc.wan.widget.indicator.ScaleTransitionPagerTitleView
import kotlinx.android.synthetic.main.fragment_square.*
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
 * @Description: 广场页面
 */
class SquareFragment : BaseMvpFragment<SquareContract.View, SquarePresenter>(),
    SquareContract.View {

    companion object {

        fun newInstance(): SquareFragment {
            return SquareFragment()
        }
    }

    override fun createPresenter(): SquarePresenter {
        return SquarePresenter()
    }

    override fun createView(): SquareContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_square
    }

    override fun initView() {
        super.initView()

        flIndicator.setBackgroundColor(SettingManager.getThemeColor())
    }

    override fun initTitleBar() {
        super.initTitleBar()

        titleBar.visibility = View.GONE
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter()?.loadSquareTitles()
    }

    override fun showIndicator(titleList: MutableList<String>) {
        // ViewPager init
        val fragments = ArrayList<Fragment>()

        fragments.run {
            add(PlazaFragment.newInstance())
            add(SystemFragment.newInstance())
            add(NavigationFragment.newInstance())
        }

        val adapter = MyViewPagerAdapter(childFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size


        // Indicator init
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(titleList[index])
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
            flIndicator.setBackgroundColor(SettingManager.getThemeColor())
        }
    }

}
