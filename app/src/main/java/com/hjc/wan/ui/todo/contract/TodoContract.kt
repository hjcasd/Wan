package com.hjc.wan.ui.todo.contract

import com.hjc.baselib.base.IBaseView
import com.hjc.wan.model.TodoBean

interface TodoContract {

    interface View : IBaseView {
        fun showList(result: MutableList<TodoBean>)
        fun refreshList()
        fun refreshComplete()
    }

    interface Presenter {
        fun loadListData(page: Int, isFirst: Boolean)
        fun deleteTodo(id: Int)
        fun finishTodo(id: Int)
    }
}
