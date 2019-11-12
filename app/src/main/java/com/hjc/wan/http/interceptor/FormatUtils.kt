package com.hjc.wan.http.interceptor

import com.blankj.utilcode.util.LogUtils


/**
 * @Author: HJC
 * @Date: 2019/3/7 16:33
 * @Description: 格式化电影工具类
 */

object FormatUtils {

    /**
     * 将json格式化并在logcat中输出
     * @param jsonStr json字符串
     */
    fun formatJsonAndLog(jsonStr: String) {
        var level = 0
        val sb = StringBuffer()
        for (i in 0 until jsonStr.length) {
            val c = jsonStr[i]
            if (level > 0 && '\n' == sb[sb.length - 1]) {
                sb.append(getLevelStr(level))
            }
            when (c) {
                '{', '[' -> {
                    sb.append(c + "\n")
                    level++
                }
                ',' -> sb.append(c + "\n")
                '}', ']' -> {
                    sb.append("\n")
                    level--
                    sb.append(getLevelStr(level))
                    sb.append(c)
                }
                else -> sb.append(c)
            }
        }

        val formatJson = sb.toString()

        var index = 0 // 当前位置
        var max = 3800// 需要截取的最大长度,别用4000
        var sub: String    // 进行截取操作的string
        while (index < formatJson.length) {
            if (formatJson.length < max) { // 如果长度比最大长度小
                max = formatJson.length   // 最大长度设为length,全部截取完成.
                sub = formatJson.substring(index, max)
            } else {
                sub = formatJson.substring(index, max)
            }
            LogUtils.e(sub)         // 进行输出
            index = max
            max += 3800
        }
    }

    private fun getLevelStr(level: Int): String {
        val levelStr = StringBuffer()
        for (levelI in 0 until level) {
            levelStr.append("\t")
        }
        return levelStr.toString()
    }

}
