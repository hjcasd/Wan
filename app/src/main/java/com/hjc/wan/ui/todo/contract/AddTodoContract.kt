package com.hjc.wan.ui.todo.contract

import com.hjc.baselib.base.IBaseView

interface AddTodoContract {

    interface View : IBaseView {
        fun showDatePicker()
        fun showPriorityDialog()
        fun preSubmit()
    }

    interface Presenter {
        fun submitTodo(
            title: String,
            content: String,
            date: String,
            type: Int,
            priority: Int
        )

        fun editTodo(
            title: String,
            content: String,
            date: String,
            type: Int,
            priority: Int,
            id: Int
        )
    }
}
