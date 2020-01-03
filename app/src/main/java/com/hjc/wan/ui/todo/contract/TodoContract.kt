package com.hjc.wan.ui.todo.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.TodoBean

interface TodoContract {

    interface View : IBaseView {
        fun showList(result: MutableList<TodoBean>)
        fun refreshList()
    }

    interface Presenter {
        fun loadListData(page: Int)
        fun deleteTodo(id: Int)
        fun finishTodo(id: Int)
    }
}
