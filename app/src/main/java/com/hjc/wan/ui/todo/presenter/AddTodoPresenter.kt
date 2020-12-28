package com.hjc.wan.ui.todo.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.todo.contract.AddTodoContract

class AddTodoPresenter : KotlinPresenter<AddTodoContract.View>(), AddTodoContract.Presenter {

    override fun submitTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ) {
        launchWrapper({
            RetrofitClient.getApi().addTodo(title, content, date, type, priority)
        }, {
            ToastUtils.showShort("添加成功")
            getView()?.setResult()
        }, true)
    }

    override fun editTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int,
        id: Int
    ) {
        launchWrapper({
            RetrofitClient.getApi().updateTodo(title, content, date, type, priority, id)
        }, {
            ToastUtils.showShort("编辑成功")
            getView()?.setResult()
        }, true)
    }

}