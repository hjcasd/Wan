package com.hjc.wan.ui.todo.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.baselib.base.BasePresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.http.bean.BasePageResponse
import com.hjc.wan.http.helper.RxHelper
import com.hjc.wan.http.observer.CommonObserver
import com.hjc.wan.http.observer.ProgressObserver
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.TodoListActivity
import com.hjc.wan.ui.todo.contract.TodoContract

class TodoPresenter : BasePresenter<TodoContract.View>(), TodoContract.Presenter {

    override fun loadListData(page: Int) {
        val activity = getView() as TodoListActivity

        RetrofitClient.getApi()
            .getTodoData(page)
            .compose(RxHelper.bind(activity))
            .subscribe(object : CommonObserver<BasePageResponse<MutableList<TodoBean>>>() {

                override fun onSuccess(result: BasePageResponse<MutableList<TodoBean>>?) {
                    val data = result?.datas
                    data?.let {
                        if (data.size > 0) {
                            getView()?.showContent()
                            getView()?.showList(data)
                        } else {
                            if (page == 1) {
                                getView()?.showEmpty()
                            } else {
                                getView()?.showContent()
                                ToastUtils.showShort("没有更多数据了")
                            }
                        }
                    }
                }

                override fun onFailure(errorMsg: String) {
                    super.onFailure(errorMsg)
                    if (errorMsg == "网络不可用" || errorMsg == "请求网络超时") {
                        getView()?.showNoNetwork()
                    } else {
                        getView()?.showError()
                    }
                }

            })
    }

    override fun deleteTodo(id: Int) {
        val activity = getView() as TodoListActivity

        RetrofitClient.getApi()
            .deleteTodo(id)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    ToastUtils.showShort("删除成功")
                    getView()?.refreshList()
                }

            })
    }

    override fun finishTodo(id: Int) {
        val activity = getView() as TodoListActivity

        RetrofitClient.getApi()
            .doneTodo(id, 1)
            .compose(RxHelper.bind(activity))
            .subscribe(object : ProgressObserver<Any>(activity.supportFragmentManager) {

                override fun onSuccess(result: Any?) {
                    getView()?.refreshList()
                }

            })
    }

}