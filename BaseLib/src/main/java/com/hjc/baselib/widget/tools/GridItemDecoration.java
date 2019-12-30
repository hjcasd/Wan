package com.hjc.baselib.widget.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * @Author: HJC
 * @Date: 2019/1/7 11:29
 * @Description: RecyclerView的网格分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    public GridItemDecoration(Context context, int resId) {
        //获取 Drawable 对象
        mDrawable = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        //绘制水平方向
        int mChildCount = parent.getChildCount();
        for (int i = 0; i < mChildCount; i++) {
            View mChildView = parent.getChildAt(i);
            RecyclerView.LayoutParams mLayoutParams = (RecyclerView.LayoutParams) mChildView.getLayoutParams();
            int left = mChildView.getLeft() - mLayoutParams.leftMargin;
            int right = mChildView.getRight() + mDrawable.getIntrinsicWidth() + mLayoutParams.rightMargin;
            int top = mChildView.getBottom() + mLayoutParams.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(canvas);
        }

        //绘制垂直方向
        for (int i = 0; i < mChildCount; i++) {
            View mChildView = parent.getChildAt(i);
            RecyclerView.LayoutParams mLayoutParams = (RecyclerView.LayoutParams) mChildView.getLayoutParams();
            int left = mChildView.getRight() + mLayoutParams.rightMargin;
            int right = left + mDrawable.getIntrinsicWidth();
            int top = mChildView.getTop() - mLayoutParams.topMargin;
            int bottom = mChildView.getBottom() + mLayoutParams.bottomMargin;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int bottom = mDrawable.getIntrinsicHeight();
        int right = mDrawable.getIntrinsicWidth();
        //如果是最后一列，留出位置
        if (isLastCol(view, parent)) {
            right = 0;
        }
        // 如果是最后一行，留出位置
        if (isLastRow(view, parent)) {
            bottom = 0;
        }
        outRect.bottom = bottom;
        outRect.right = right;
    }

    /**
     * 是否是最后一列
     * （当前位置+1）% 列数 ==0，判断是否为最后一列
     *
     * @return
     */
    private boolean isLastCol(View view, RecyclerView parent) {
        int currentPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int mSpanCount = getSpanCount(parent);
        return (currentPosition + 1) % mSpanCount == 0;
    }

    /**
     * 是否是最后一行
     * 当前位置+1 > (行数-1)*列数
     *
     * @return
     */
    private boolean isLastRow(View view, RecyclerView parent) {
        int currentPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int mSpanCount = getSpanCount(parent);
        int rowNum = parent.getAdapter().getItemCount() / mSpanCount == 0 ? parent.getAdapter().getItemCount() / mSpanCount : (parent.getAdapter().getItemCount() / mSpanCount + 1);
        return (currentPosition + 1) > (rowNum - 1) * mSpanCount;
    }

    /**
     * 获取列数
     * 如果是GridView，就获取列数，如果是ListView，列数就是1
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager mLayoutManager = parent.getLayoutManager();
        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager mGridLayoutManager = (GridLayoutManager) mLayoutManager;
            return mGridLayoutManager.getSpanCount();
        }
        return 1;
    }

}