package com.hjc.baselib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.hjc.baselib.utils.ClickUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:58
 * @Description: Fragment基类
 */
public abstract class BaseFragment extends RxFragment implements View.OnClickListener {
	/**
	 * Fragment对应的Activity(避免使用getActivity()导致空指针异常)
	 */
	protected Context mContext;

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getLayoutId(), container, false);
	}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData(savedInstanceState);
        addListeners();
    }

    /**
	 * 获取布局的ID
	 */
	public abstract int getLayoutId();

	/**
	 * 初始化View
	 */
	protected void initView(){

	}

	/**
	 * 初始化数据
	 */
	public abstract void initData(@Nullable Bundle savedInstanceState);

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
}
