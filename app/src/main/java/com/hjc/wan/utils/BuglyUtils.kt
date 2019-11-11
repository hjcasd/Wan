package com.hjc.wan.utils

import android.app.Application
import android.content.Context
import android.text.TextUtils

import com.hjc.wan.BuildConfig
import com.tencent.bugly.crashreport.CrashReport

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @Author: HJC
 * @Date: 2019/11/2 15:40
 * @Description: 初始化Bugly工具类
 */
object BuglyUtils {

    fun init(context: Context) {
        if (context is Application) {
            // 获取当前包名
            val packageName = context.getPackageName()
            // 获取当前进程名
            val processName = getProcessName(android.os.Process.myPid())
            // 设置是否为上报进程
            val strategy = CrashReport.UserStrategy(context)
            strategy.isUploadProcess = processName == null || processName == packageName
            CrashReport.initCrashReport(context, "04002332f3", BuildConfig.IS_DEBUG, strategy)
        } else {
            throw UnsupportedOperationException("context must be application...")
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

}
