package com.hjc.baselib.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.hjc.baselib.utils.ClickUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:58
 * @Description: Fragment基类(用于懒加载)
 */
public abstract class BaseLazyFragment extends RxFragment implements View.OnClickListener {
	/**
	 * Fragment对应的Activity(避免使用getActivity()导致空指针异常)
	 */
	protected Context mContext;

    /**
     * 判断View是否加载完成
     */
    private boolean isPrepared;

    /**
     * 判断当前Fragment是否可见
     */
    private boolean isFragmentVisible;

    /**
     * 判断是否第一次加载数据
     */
    private boolean isFirstLoad = true;

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isFragmentVisible = true;
            lazyLoad();
        } else {
            isFragmentVisible = false;
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
	}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ARouter.getInstance().inject(this);

        isPrepared = true;
        lazyLoad();
    }

    /**
     * 懒加载
     */
    private void lazyLoad() {
        if (isPrepared && isFragmentVisible && isFirstLoad) {
            isFirstLoad = false;
            initView();
            initData();
            addListeners();
        }
    }

    /**
	 * 获取布局的ID
	 */
	public abstract int getLayoutId();

    /**
     * 初始化View
     */
    public abstract void initView();

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 设置监听器
	 */
	public abstract void addListeners();

	/**
	 * 设置点击事件
	 */
	public abstract void onSingleClick(View v);

	@Override
	public void onClick(View view) {
        //避免快速点击
        if (ClickUtils.isFastClick()){
            ToastUtils.showShort("点的太快了,歇会呗!");
            return;
        }
		onSingleClick(view);
	}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }
}
