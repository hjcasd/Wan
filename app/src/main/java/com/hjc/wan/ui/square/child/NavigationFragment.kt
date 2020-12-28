package com.hjc.wan.ui.square.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjc.baselib.fragment.BaseMvpLazyFragment
import com.hjc.wan.R
import com.hjc.wan.model.NavigationBean
import com.hjc.wan.ui.square.adapter.NavigationContentAdapter
import com.hjc.wan.ui.square.adapter.NavigationMenuAdapter
import com.hjc.wan.ui.square.contract.NavigationContract
import com.hjc.wan.ui.square.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_navigation.*
import java.util.*

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

        initLoadSir(rlRoot)

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
        //点击侧边栏菜单,滑动到指定位置
        mNavigationMenuAdapter.setOnSelectListener(object : NavigationMenuAdapter.OnSelectListener{
            override fun onSelected(position: Int) {
                contentManager.scrollToPositionWithOffset(position, 0)
            }

        })

        //侧边栏菜单随右边列表一起滚动
        rvNavigationContent.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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

    override fun onSingleClick(v: View?) {

    }

    override fun onRetryBtnClick(v: View?) {
        super.onRetryBtnClick(v)
        getPresenter()?.loadListData()
    }

}