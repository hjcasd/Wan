package com.hjc.webviewlib;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * @Author: HJC
 * @Date: 2019/11/6 14:14
 * @Description: 文件阅读器
 */
public class FileReaderView extends FrameLayout implements TbsReaderView.ReaderCallback {

    private TbsReaderView mTbsReaderView;
    private Context context;

    public FileReaderView(Context context) {
        this(context, null, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mTbsReaderView = getTbsReaderView(context);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
    }

    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    /**
     * 初始化完布局调用此方法浏览文件
     * 注意: 只支持加载本地文件,远程文件需先下载后再浏览
     *
     * @param filePath 文件路径
     */
    public void show(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            String tempPath = Environment.getExternalStorageDirectory() + File.separator + "TbsReaderTemp";
            //加载文件
            Bundle localBundle = new Bundle();
            localBundle.putString("filePath", filePath);
            localBundle.putString("tempPath", tempPath);
            if (this.mTbsReaderView == null) {
                this.mTbsReaderView = getTbsReaderView(context);
            }
            boolean bool = mTbsReaderView.preOpen(getFileType(filePath), false);
            if (bool) {
                this.mTbsReaderView.openFile(localBundle);
            }
        } else {
            Log.e("FileReaderView", "文件路径无效！");
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 务必在onDestroy方法中调用此方法，否则第二次打开无法浏览
     */
    public void stop() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    /***
     * 获取文件类型
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

}
