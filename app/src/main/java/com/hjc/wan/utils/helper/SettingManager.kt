package com.hjc.wan.utils.helper

import android.content.res.ColorStateList
import android.graphics.Color
import com.blankj.utilcode.util.SPUtils

/**
 * @Author: HJC
 * @Date: 2019/2/20 15:39
 * @Description: 设置管理类
 */
object SettingManager {

    private const val KEY_MODE = "mode"

    /**
     * 获取颜色值
     */
    fun getColorByType(type: Int): Int {
        val colors = arrayOf(
            Color.parseColor("#fe7b7b"),
            Color.parseColor("#dda239"),
            Color.parseColor("#69adef"),
            Color.parseColor("#6cc959")
        )
        return colors[type]
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 获取列表动画类型
     */
    fun getListAnimationType(): Int {
        return SPUtils.getInstance().getInt(KEY_MODE, 0)
    }

    /**
     * 设置列表动画类型
     */
    fun setListAnimationType(mode: Int) {
        SPUtils.getInstance().put(KEY_MODE, mode)
    }


}
