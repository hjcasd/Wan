package com.hjc.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @Author: HJC
 * @Date: 2019/7/18 10:43
 * @Description: 解决ViewPager bug
 */
class FixedViewPager(
    context: Context?,
    attributeSet: AttributeSet?
) : ViewPager(context!!, attributeSet) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }
}