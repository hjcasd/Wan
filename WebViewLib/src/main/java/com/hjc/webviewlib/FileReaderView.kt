package com.hjc.webviewlib

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tencent.smtt.sdk.TbsReaderView
import java.io.File

/**
 * @Author: HJC
 * @Date: 2019/11/6 14:14
 * @Description: 文件阅读器
 */
class FileReaderView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
), TbsReaderView.ReaderCallback {

    private var mTbsReaderView: TbsReaderView?

    init {
        mTbsReaderView = getTbsReaderView(context)
        this.addView(mTbsReaderView, LinearLayout.LayoutParams(-1, -1))
    }

    private fun getTbsReaderView(context: Context): TbsReaderView {
        return TbsReaderView(context, this)
    }

    /**
     * 初始化完布局调用此方法浏览文件
     * 注意: 只支持加载本地文件,远程文件需先下载后再浏览
     *
     * @param filePath 文件路径
     */
    fun show(filePath: String) {
        if (!TextUtils.isEmpty(filePath)) {
            val tempPath = Environment.getExternalStorageDirectory()
                .toString() + File.separator + "TbsReaderTemp"
            //加载文件
            val localBundle = Bundle()
            localBundle.putString("filePath", filePath)
            localBundle.putString("tempPath", tempPath)
            if (mTbsReaderView == null) {
                mTbsReaderView = getTbsReaderView(context)
            }
            val bool = mTbsReaderView!!.preOpen(getFileType(filePath), false)
            if (bool) {
                mTbsReaderView!!.openFile(localBundle)
            }
        } else {
            Log.e("FileReaderView", "文件路径无效！")
        }
    }

    override fun onCallBackAction(integer: Int, o: Any, o1: Any) {}

    /**
     * 务必在onDestroy方法中调用此方法，否则第二次打开无法浏览
     */
    fun stop() {
        if (mTbsReaderView != null) {
            mTbsReaderView!!.onStop()
        }
    }

    /***
     * 获取文件类型
     */
    private fun getFileType(paramString: String): String {
        var str = ""
        if (TextUtils.isEmpty(paramString)) {
            return str
        }
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            return str
        }
        str = paramString.substring(i + 1)
        return str
    }


}