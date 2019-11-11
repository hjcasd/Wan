package com.hjc.wan.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_login.view.*

/**
 * @Author: HJC
 * @Date: 2019/11/11 18:13
 * @Description: 自定义登录界面，监听布局高度的变化，如果高宽比小于4:3说明此时键盘弹出，应改变布局的比例结果以保证所有元素都显示出来
 */ 
class LoginLayout(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {

    private var keyboardShowed = false

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val width = right - left
            val height = bottom - top
            if (height.toFloat() / width.toFloat() < 4f / 3f) { // 如果高宽比小于4:3说明此时键盘弹出
                post {
                    llBg.visibility = View.INVISIBLE
                    val params = rlTop.layoutParams as LayoutParams
                    params.weight = 1.5f
                    keyboardShowed = true
                    rlTop.requestLayout()
                }
            } else {
                if (keyboardShowed) {
                    post {
                        llBg.visibility = View.VISIBLE
                        val params = rlTop.layoutParams as LayoutParams
                        params.weight = 6f
                        rlTop.requestLayout()
                    }
                }
            }
        }
    }

}