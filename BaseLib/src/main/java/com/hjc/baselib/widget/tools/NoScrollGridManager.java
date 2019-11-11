package com.hjc.baselib.widget.tools;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:30
 * @Description: 禁止网格RecyclerView滑动的管理器
 */
public class NoScrollGridManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollGridManager(Context context, int spanCount) {
        super(context, spanCount);
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
