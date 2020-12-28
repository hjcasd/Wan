package com.hjc.wan.ui.search.presenter

import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.common.KotlinPresenter
import com.hjc.wan.http.RetrofitClient
import com.hjc.wan.model.ArticleBean
import com.hjc.wan.model.db.History
import com.hjc.wan.model.db.HistoryDataBase
import com.hjc.wan.ui.search.SearchActivity
import com.hjc.wan.ui.search.contract.SearchContract

class SearchPresenter : KotlinPresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun getHotKey() {
        launchWrapper({
            RetrofitClient.getApi().getHotKey()
        }, { result ->
            if (result != null) {
                getView()?.showHotTag(result)
            } else {
                ToastUtils.showShort("获取热门搜索数据失败")
            }
        }, true)
    }

    override fun getHistory() {
        val activity = getView() as SearchActivity

        val historyList = HistoryDataBase.getInstance(activity).getHistoryDao().getAllHistory()
        if (historyList != null && historyList.isNotEmpty()) {
            getView()?.showHistory(historyList)
        } else {
            getView()?.hideHistory()
        }
    }

    override fun search(page: Int, key: String, isShow: Boolean) {
        saveHistory(key)

        launchWrapper({
            RetrofitClient.getApi().search(page, key)
        }, { result ->
            getView()?.refreshComplete()

            val data = result?.datas
            data?.let {
                if (data.size > 0) {
                    getView()?.showList(data)
                } else {
                    if (page == 0) {
                        ToastUtils.showShort("暂无搜索数据")
                    } else {
                        ToastUtils.showShort("没有更多数据了")
                    }
                }
            }
        }, isShow)
    }

    /**
     * 保存历史记录
     *
     * @param keyword 要保存的关键词
     */
    private fun saveHistory(keyword: String) {
        val activity = getView() as SearchActivity

        val historyDao = HistoryDataBase.getInstance(activity).getHistoryDao()
        val historyList = historyDao.getAllHistory()
        var isExit = false
        if (historyList != null) {
            for (entity in historyList) {
                if (keyword == entity.name) {
                    isExit = true
                }
            }
        }
        if (!isExit) {
            val history = History()
            history.name = keyword
            historyDao.insert(history)
        }
    }


    /**
     * 收藏
     */
    override fun collectArticle(bean: ArticleBean) {
        launchWrapper({
            RetrofitClient.getApi().collect(bean.id)
        }, {
            ToastUtils.showShort("收藏成功")
            getView()?.showCollectList(bean)
        }, true)

    }

    /**
     * 取消收藏
     */
    override fun unCollectArticle(bean: ArticleBean) {
        launchWrapper({
            RetrofitClient.getApi().unCollect(bean.id)
        }, {
            ToastUtils.showShort("已取消收藏")
            getView()?.showUnCollectList(bean)
        }, true)
    }

}