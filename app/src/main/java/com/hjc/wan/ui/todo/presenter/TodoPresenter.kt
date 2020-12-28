package com.hjc.wan.ui.todo.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.ui.todo.contract.TodoContract

class TodoPresenter : KotlinPresenter<TodoContract.View>(), TodoContract.Presenter {

    override fun loadListData(page: Int, isFirst: Boolean) {
        launchWrapper({
            RetrofitClient.getApi().getTodoData(page)
        }, { result ->
            getView()?.refreshComplete()

            val data = result?.datas
            data?.let {
                if (it.size > 0) {
                    getView()?.showContent()
                    getView()?.showList(it)
                } else {
                    if (page == 1) {
                        getView()?.showEmpty()
                    } else {
                        getView()?.showContent()
                        ToastUtils.showShort("没有更多数据了")
                    }
                }
            }
        }, isShowStatus = isFirst)
    }

    override fun deleteTodo(id: Int) {
        launchWrapper({
            RetrofitClient.getApi().deleteTodo(id)
        }, {
            ToastUtils.showShort("删除成功")
            getView()?.refreshList()
        }, true)
    }

    override fun finishTodo(id: Int) {
        launchWrapper({
            RetrofitClient.getApi().doneTodo(id, 1)
        }, {
            getView()?.refreshList()
        }, true)
    }

}