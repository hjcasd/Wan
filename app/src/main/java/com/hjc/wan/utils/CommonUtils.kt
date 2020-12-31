package com.hjc.wan.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils

/**
 * @Author: HJC
 * @Date: 2019/1/28 15:33
 * @Description: 常用方法封装
 */
object CommonUtils {

    /**
     * 实现文本复制功能
     *
     * @param context 上下文
     * @param text    复制的文本
     */
    fun copy(context: Context, text: String) {
        if (!TextUtils.isEmpty(text)) {
            //获取剪贴板管理器：
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val clipData = ClipData.newPlainText("Label", text)
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(clipData)
        }
    }

    /**
     * 使用浏览器打开链接
     *
     * @param context 上下文
     * @param link    链接地址
     */
    fun openLink(context: Context, link: String) {
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
    fun share(context: Context, extraText: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent.putExtra(Intent.EXTRA_TEXT, extraText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent, "分享"))
    }
}
