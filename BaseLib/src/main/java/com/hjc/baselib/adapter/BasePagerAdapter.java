package com.hjc.baselib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:51
 * @Description: PagerAdapter基类
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected Context mContext;
    private SparseArray<View> mViews;
    protected List<T> mDataList;

    private onItemClickListener onItemClickListener;

    public BasePagerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mDataList = list;
        mViews = new SparseArray<>(list.size());
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = View.inflate(mContext, getLayoutId(), null);
            initView(view, position);
            mViews.put(position, view);
        }

        view.setOnClickListener(v -> {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(position);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View itemView, int position);


    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
