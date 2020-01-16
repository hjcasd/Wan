package com.hjc.wan.widget;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @Author: HJC
 * @Date: 2019/1/7 11:31
 * @Description: 搜索EditText
 */
public class SearchEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener, View.OnKeyListener {
    private Drawable mClearDrawable;           //保存 EditText右侧的删除按钮
    private boolean hasFocus;                  //保存控件是否获取到焦点

    private OnSearchClickListener listener;

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获取右侧清除图标
        mClearDrawable = getCompoundDrawables()[2];
        setClearIconVisible(false);

        setOnFocusChangeListener(this);
        setOnKeyListener(this);
        addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
        if (s.length() == 0) {
            if (listener != null) {
                listener.onSearchClear();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (getCompoundDrawables()[2] != null) { //清除按钮存在时
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height(); //按钮高
                int distance = (getHeight() - height) / 2; //按钮距离上边缘（下边缘）的距离
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                    if (listener != null) {
                        listener.onSearchClear();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && listener != null) {
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                listener.onSearchClick(v);
            }
        }
        return false;
    }

    /**
     * 设置是否显示隐藏图标
     *
     * @param visible 是否显示
     */
    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view);

        void onSearchClear();
    }

    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.listener = listener;
    }
}

