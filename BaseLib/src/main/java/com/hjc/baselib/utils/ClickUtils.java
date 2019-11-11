package com.hjc.baselib.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:34
 * @Description: 点击工具类
 */
public class ClickUtils {
    private static Map<String, Long> records = new HashMap<>();
    private static Map<String, Long> record2s = new HashMap<>();

    public static boolean isFastClick() {
        if (records.size() > 1000) {
            records.clear();
        }

        //本方法被调用的文件名和行号作为标记
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String key = ste.getFileName() + ste.getLineNumber();

        Long lastClickTime = records.get(key);
        long thisClickTime = System.currentTimeMillis();
        records.put(key, thisClickTime);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        long timeDuration = thisClickTime - lastClickTime;

        //如果两次点击间隔在500ms之内,则为快速点击
        return 0 < timeDuration && timeDuration < 500;
    }

    public static boolean isDoubleClick() {
        if (record2s.size() > 1000) {
            record2s.clear();
        }

        //本方法被调用的文件名和行号作为标记
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String key = ste.getFileName() + ste.getLineNumber();

        Long lastClickTime = record2s.get(key);
        long thisClickTime = System.currentTimeMillis();
        record2s.put(key, thisClickTime);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        long timeDuration = thisClickTime - lastClickTime;

        //如果两次点击间隔在2000ms之内,则为双击
        return 0 < timeDuration && timeDuration < 2000;
    }
}
