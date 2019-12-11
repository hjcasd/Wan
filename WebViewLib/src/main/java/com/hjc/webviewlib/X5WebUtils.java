package com.hjc.webviewlib;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * @Author: HJC
 * @Date: 2019/11/2 15:42
 * @Description: X5WebView初始化工具类
 */
public final class X5WebUtils {

    private X5WebUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context){
        if(context instanceof Application){
            // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    // x5內核初始化完成的回调，为true表示x5内核加载成功,否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("X5WebView", " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    Log.d("X5WebView", " onCoreInitFinished");
                }
            };
            // x5内核初始化接口
            QbSdk.initX5Environment(context,  cb);
        }else {
            throw new UnsupportedOperationException("context must be application...");
        }
    }
}
