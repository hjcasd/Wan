package com.hjc.baselib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

/**
 * @Author: HJC
 * @Date: 2019/7/1 17:52
 * @Description: 公共工具类
 */
object CommonUtils {
    /**
     * 实现文本复制功能
     *
     * @param context 上下文
     * @param text    复制的文本
     */
    fun copy(context: Context, text: String?) {
        if (!TextUtils.isEmpty(text)) { //获取剪贴板管理器：
            val cm =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", text)
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
        }
    }

    /**
     * 使用浏览器打开链接
     *
     * @param context 上下文
     * @param link    链接地址
     */
    fun openLink(context: Context, link: String?) {
        val issuesUrl = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, issuesUrl)
        context.startActivity(intent)
    }

    /**
     * 分享
     *
     * @param context   上下文
     * @param extraText 内容
     */
    fun share(context: Context, extraText: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent.putExtra(Intent.EXTRA_TEXT, extraText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent, "分享"))
    }

    /**
     * MD5加密
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    fun md5(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return ""
        }
        try {
            val digest = MessageDigest.getInstance("MD5")
            val bytes = digest.digest(str.toByteArray())
            val result = StringBuilder()
            for (b in bytes) {
                var temp = Integer.toHexString((b and 0xff.toByte()).toInt())
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 合并数组
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @param <T>    T类型
     * @return 新数组
    </T> */
    fun <T> concat(first: Array<T>, second: Array<T>): Array<T> {
        val result = Arrays.copyOf(first, first.size + second.size)
        System.arraycopy(second, 0, result, first.size, second.size)
        return result
    }
}