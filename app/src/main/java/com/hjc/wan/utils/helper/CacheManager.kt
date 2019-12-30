package com.hjc.wan.utils.helper

import android.content.Context
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.blankj.utilcode.util.FileUtils
import com.tencent.smtt.sdk.WebView
import java.math.BigDecimal


object CacheManager {

    /**
     * 获取缓存大小
     */
    fun getTotalCacheSize(context: Context): String {
        return getFormatSize(getCacheDirSize(context).toDouble())
    }

    private fun getCacheDirSize(context: Context): Long {
        var cacheSize = FileUtils.getDirLength(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += FileUtils.getDirLength(context.externalCacheDir)
        }
        return cacheSize
    }

    /**
     * 清除缓存
     */
    fun clearTotalCache(context: Context) {
        try {
            clearDirCache(context)
//            clearWebViewCache(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearDirCache(context: Context) {
        FileUtils.deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            context.externalCacheDir?.let {
                FileUtils.deleteDir(it)
            }
        }
    }

    private fun clearWebViewCache(context: Context) {
        //安卓自带浏览器内核
        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        //X5浏览器内核
        CookieSyncManager.createInstance(context)
        val x5cookieManager = CookieManager.getInstance()
        x5cookieManager.removeAllCookies(null)
        //删除浏览器相关数据库
        //删除浏览器相关数据库
        context.deleteDatabase("webview.db")
        context.deleteDatabase("webviewCache.db")
        WebView(context).clearCache(true)
        WebView(context).clearCache(true)
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "B"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}