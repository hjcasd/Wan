package com.hjc.wan.ui.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpFragment
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.IntegralBean
import com.hjc.wan.ui.mine.contract.MineContract
import com.hjc.wan.ui.mine.presenter.MinePresenter
import com.hjc.wan.utils.helper.RouterManager
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

        getPresenter()?.loadIntegralData(true)
    }


    @SuppressLint("SetTextI18n")
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
        llTodo.setOnClickListener(this)

        llOpen.setOnClickListener(this)
        llJoin.setOnClickListener(this)
        llSetting.setOnClickListener(this)

        smartRefreshLayout.setOnRefreshListener {
            getPresenter()?.loadIntegralData(false)
        }
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)
        when (v?.id) {
            R.id.llIntegral -> {
                RouterManager.jump(RoutePath.URL_INTEGRAL_RANK)
            }
            R.id.llCollect -> {
                RouterManager.jump(RoutePath.URL_COLLECT)
            }
            R.id.llTodo -> {
                RouterManager.jump(RoutePath.URL_TO_DO)
            }

            R.id.llOpen -> {
                RouterManager.jumpToWeb("玩Android网站", "https://www.wanandroid.com/")
            }
            R.id.llJoin -> {
                val key = "arD6CZ5Bt8_ReDpb56-5n5cIY6sBqTtl"
                val intent = Intent()
                intent.data =
                    Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    // 未安装手Q或安装的版本不支持
                    ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
                }
            }
            R.id.llSetting -> {
                RouterManager.jump(RoutePath.URL_SETTING)
            }
        }
    }

}
