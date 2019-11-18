package com.hjc.wan.ui.mine

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpFragment
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.mine.contract.MineContract
import com.hjc.wan.ui.mine.presenter.MinePresenter
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @Author: HJC
 * @Date: 2019/11/9 15:38
 * @Description: 我的页面
 */
class MineFragment : BaseMvpFragment<MineContract.View, MinePresenter>(), MineContract.View {

    companion object {

        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun createPresenter(): MinePresenter {
        return MinePresenter()
    }

    override fun createView(): MineContract.View {
        return this
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        getPresenter().getIntegralInfo(true)
    }


    override fun showIntegral(result: IntegralBean) {
        smartRefreshLayout.finishRefresh()
        tvName.text = result.username
        tvInfo.text = "id ： ${result.userId}　排名 ： ${result.rank}"
        tvIntegral.text = result.coinCount.toString()
    }

    override fun addListeners() {
        super.addListeners()

        llIntegral.setOnClickListener(this)
        llCollect.setOnClickListener(this)
        llArticle.setOnClickListener(this)
        llTodo.setOnClickListener(this)

        llOpen.setOnClickListener(this)
        llJoin.setOnClickListener(this)
        llSetting.setOnClickListener(this)

        smartRefreshLayout.setOnRefreshListener {
            getPresenter().getIntegralInfo(false)
        }
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)
        when (v?.id) {
            R.id.llIntegral -> {
                ToastUtils.showShort("我的积分")
            }
            R.id.llCollect -> {
                ToastUtils.showShort("我的收藏")
            }
            R.id.llArticle -> {
                ToastUtils.showShort("我的文章")
            }
            R.id.llTodo -> {
                ToastUtils.showShort("待办事项")
            }

            R.id.llOpen -> {
                ToastUtils.showShort("开源网站")
            }
            R.id.llJoin -> {
                ToastUtils.showShort("加入我们")
            }
            R.id.llSetting -> {
                ToastUtils.showShort("系统设置")
            }
        }
    }


    override fun toIntegral() {
    }

    override fun toCollect() {
    }

    override fun toArticle() {
    }

    override fun toTodo() {
    }


}
