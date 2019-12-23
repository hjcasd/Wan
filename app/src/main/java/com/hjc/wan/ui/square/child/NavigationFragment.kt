package com.hjc.wan.ui.square.child

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpLazyFragment
import com.hjc.wan.http.helper.RxSchedulers
import com.hjc.wan.model.NavigationBean
import com.hjc.wan.ui.square.adapter.NavigationContentAdapter
import com.hjc.wan.ui.square.adapter.NavigationMenuAdapter
import com.hjc.wan.ui.square.contract.NavigationContract
import com.hjc.wan.ui.square.presenter.NavigationPresenter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_navigation.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author: HJC
 * @Date: 2019/11/14 14:38
 * @Description: 导航子页面
 */
class NavigationFragment : BaseMvpLazyFragment<NavigationContract.View, NavigationPresenter>(),
    NavigationContract.View {

    private lateinit var mNavigationMenuAdapter: NavigationMenuAdapter
    private lateinit var mNavigationContentAdapter: NavigationContentAdapter

    private lateinit var contentManager: LinearLayoutManager

    private var oldPosition = 0

    companion object {

        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }

    override fun createPresenter(): NavigationPresenter {
        return NavigationPresenter()
    }

    override fun createView(): NavigationContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation
    }

    override fun initView() {
        super.initView()

        val manager = LinearLayoutManager(mContext)
        rvNavigationMenu.layoutManager = manager
        mNavigationMenuAdapter = NavigationMenuAdapter(null)
        rvNavigationMenu.adapter = mNavigationMenuAdapter

        contentManager = LinearLayoutManager(mContext)
        rvNavigationContent.layoutManager = contentManager
        mNavigationContentAdapter = NavigationContentAdapter(null)
        rvNavigationContent.adapter = mNavigationContentAdapter
    }

    override fun initData() {
        super.initData()

        showLoading()
        getPresenter()?.loadListData()
    }

    override fun showList(result: MutableList<NavigationBean>) {
        val chapterList = ArrayList<String>()
        for (bean in result) {
            chapterList.add(bean.name)
        }
        mNavigationMenuAdapter.setNewData(chapterList)
        mNavigationMenuAdapter.setSelection(0)
        mNavigationContentAdapter.setNewData(result)

    }

    override fun addListeners() {
        super.addListeners()

        //点击侧边栏菜单,滑动到指定位置
        mNavigationMenuAdapter.setOnSelectListener(object : NavigationMenuAdapter.OnSelectListener{
            override fun onSelected(position: Int) {
                contentManager.scrollToPositionWithOffset(position, 0)
            }

        })

        //侧边栏菜单随右边列表一起滚动
        rvNavigationContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstPosition = contentManager.findFirstVisibleItemPosition()
                if (oldPosition != firstPosition) {
                    rvNavigationMenu.smoothScrollToPosition(firstPosition)
                    mNavigationMenuAdapter.setSelection(firstPosition)
                    oldPosition = firstPosition
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    override fun showContent() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
            .compose(RxSchedulers.ioToMain())
            .subscribe {
                stateView.showContent()
                smartRefreshLayout.finishRefresh()
            }
    }

    override fun showLoading() {
        stateView.showLoading()
    }

    override fun showError() {
        stateView.showError()
        smartRefreshLayout.finishRefresh()
    }

    override fun showEmpty() {
        stateView.showEmpty()
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoNetwork() {
        stateView.showNoNetwork()
        smartRefreshLayout.finishRefresh()
    }

}