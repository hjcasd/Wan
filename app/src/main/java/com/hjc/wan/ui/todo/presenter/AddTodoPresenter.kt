package com.hjc.wan.ui.todo.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.ui.todo.child.AddTodoActivity
import com.hjc.wan.ui.todo.contract.AddTodoContract

class AddTodoPresenter : BasePresenter<AddTodoContract.View>(), AddTodoContract.Presenter {

    override fun submitTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ) {
        val activity = getView() as AddTodoActivity

        RetrofitClient.getApi()
            .addTodo(title, content, date, type, priority)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("添加成功")
                    activity.setResult(1000)
                    activity.finish()
                }

            })
    }

    override fun editTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int,
        id: Int
    ) {
        val activity = getView() as AddTodoActivity

        RetrofitClient.getApi()
            .updateTodo(title, content, date, type, priority, id)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("编辑成功")
                    activity.setResult(1000)
                    activity.finish()
                }

            })
    }

}