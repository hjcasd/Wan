package com.hjc.baselib.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.hjc.baselib.fragment.BaseFragment;
import com.hjc.baselib.utils.ClickUtils;
import com.hjc.baselib.utils.helper.ActivityManager;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:47
 * @Description: (含有Fragment)Activity基类
 */
public abstract class BaseFragmentActivity extends RxFragmentActivity implements View.OnClickListener {
    private Fragment mCurrentFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ActivityManager.addActivity(this);

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
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听器
     */
    public abstract void addListeners();

    /**
     * 布局中Fragment的容器ID
     */
    protected abstract int getFragmentContentId();

    /**
     * 设置点击事件
     */
    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        //避免快速点击
        if (ClickUtils.isFastClick()){
            ToastUtils.showShort("点的太快了,歇会呗!");
            return;
        }
        onSingleClick(v);
    }


    /**
     * 显示fragment
     */
    protected void showFragment(BaseFragment fragment) {
        if (mCurrentFragment != fragment) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(mCurrentFragment);

            mCurrentFragment = fragment;
            if (!fragment.isAdded()) {
                ft.add(getFragmentContentId(), fragment).show(fragment).commit();
            } else {
                ft.show(fragment).commit();
            }
        }
    }
}
