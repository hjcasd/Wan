package com.hjc.baselib.widget.tools;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:31
 * @Description: 禁止水平,垂直RecyclerView滑动的管理器
 */
public class NoScrollLinearManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollLinearManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
