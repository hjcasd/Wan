package com.hjc.wan.ui.todo.contract

import com.hjc.wan.base.IBaseView
import com.hjc.wan.model.TodoBean

interface TodoContract {

    interface View : IBaseView {
        fun showList(result: MutableList<TodoBean>)
    }

    interface Presenter {
        fun loadListData(page: Int)
    }
}
