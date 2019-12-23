package com.hjc.baselib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.hjc.baselib.utils.ClickUtils;
import com.hjc.baselib.utils.helper.ActivityManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:47
 * @Description: Activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ActivityManager.addActivity(this);
        ARouter.getInstance().inject(this);

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
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听器
     */
    public abstract void addListeners();

    /**
     * 设置点击事件
     */
    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        //避免快速点击
        if (ClickUtils.isFastClick()) {
            ToastUtils.showShort("点的太快了,歇会呗!");
            return;
        }
        onSingleClick(v);
    }
}
