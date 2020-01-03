package com.hjc.baselib.utils

import java.util.*

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:34
 * @Description: 点击工具类
 */
object ClickUtils {
    private val records: MutableMap<String, Long> = HashMap()
    private val record2s: MutableMap<String, Long> = HashMap()

    //本方法被调用的文件名和行号作为标记
    fun isFastClick(): Boolean {
        if (records.size > 1000) {
            records.clear()
        }

        //本方法被调用的文件名和行号作为标记
        val ste = Throwable().stackTrace[1]
        val key = ste.fileName + ste.lineNumber
        var lastClickTime = records[key]
        val thisClickTime = System.currentTimeMillis()
        records[key] = thisClickTime
        if (lastClickTime == null) {
            lastClickTime = 0L
        }

        val timeDuration = thisClickTime - lastClickTime
        //如果两次点击间隔在500ms之内,则为快速点击
        return timeDuration in 1..499
    }

    //本方法被调用的文件名和行号作为标记
    fun isDoubleClick(): Boolean {
        if (record2s.size > 1000) {
            record2s.clear()
        }
        //本方法被调用的文件名和行号作为标记
        val ste = Throwable().stackTrace[1]
        val key = ste.fileName + ste.lineNumber
        var lastClickTime = record2s[key]
        val thisClickTime = System.currentTimeMillis()
        record2s[key] = thisClickTime
        if (lastClickTime == null) {
            lastClickTime = 0L
        }
        val timeDuration = thisClickTime - lastClickTime
        //如果两次点击间隔在2000ms之内,则为双击
        return timeDuration in 1..1999
    }
}