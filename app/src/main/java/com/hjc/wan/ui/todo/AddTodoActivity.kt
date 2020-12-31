package com.hjc.wan.ui.todo

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gyf.immersionbar.ImmersionBar
import com.hjc.baselib.activity.BaseActivity
import com.hjc.baselib.widget.bar.OnBarLeftClickListener
import com.hjc.wan.R
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.databinding.ActivityAddTodoBinding
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.adapter.PriorityAdapter
import com.hjc.wan.ui.todo.contract.AddTodoContract
import com.hjc.wan.ui.todo.presenter.AddTodoPresenter
import com.hjc.wan.utils.helper.SettingManager
import java.util.*


/**
 * @Author: HJC
 * @Date: 2019/12/24 14:06
 * @Description: 添加待办清单页面
 */
@Route(path = RoutePath.URL_ADD_TO_DO)
class AddTodoActivity : BaseActivity<ActivityAddTodoBinding, AddTodoContract.View, AddTodoPresenter>(),
    AddTodoContract.View {

    @Autowired(name = "params")
    @JvmField
    var bundle: Bundle? = null

    private var mPriority: Int = 0

    private var mFrom: Int = 0

    private var mId: Int = 0

    private val mTitles = mutableListOf("重要且紧急", "重要但不紧急", "紧急但不重要", "不重要不紧急")

    override fun createPresenter(): AddTodoPresenter {
        return AddTodoPresenter()
    }

    override fun createView(): AddTodoContract.View {
        return this
    }

    override fun getImmersionBar(): ImmersionBar? {
        return ImmersionBar.with(this)
            .statusBarColor(ColorUtils.int2RgbString(SettingManager.getThemeColor()))
            .fitsSystemWindows(true)
    }

    override fun initView() {
        super.initView()

        SettingManager.getThemeColor().let {
            mBinding.titleBar.setBgColor(it)

            val drawable = mBinding.btnSubmit.background as GradientDrawable
            drawable.setColor(it)
        }
    }


    override fun initData(savedInstanceState: Bundle?) {
        bundle?.let {
            mFrom = it.getInt("from")
            if (mFrom == 1) {
                mBinding.titleBar.setTitle("编辑待办事项")

                val bean = it.getSerializable("bean") as TodoBean
                mId = bean.id
                mBinding.etTitle.setText(bean.title)
                mBinding.etContent.setText(bean.content)
                mBinding.tvTime.text = bean.dateStr

                mPriority = bean.priority
                mBinding.tvPriority.text = mTitles[mPriority]

                mBinding.colorView.setViewColor(SettingManager.getColorByType(mPriority))
            } else {
                mBinding.colorView.setViewColor(SettingManager.getColorByType(0))
                mBinding.titleBar.setTitle("添加待办事项")
            }
        }
    }

    override fun addListeners() {
        mBinding.tvTime.setOnClickListener(this)
        mBinding.llPriority.setOnClickListener(this)
        mBinding.btnSubmit.setOnClickListener(this)

        mBinding.titleBar.setOnBarLeftClickListener(object : OnBarLeftClickListener {

            override fun leftClick(view: View) {
                finish()
            }

        })
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.tv_time -> {
                showDatePicker()
            }

            R.id.ll_priority -> {
                showPriorityDialog()
            }

            R.id.btn_submit -> {
                preSubmit()
            }
        }
    }

    override fun showDatePicker() {
        MaterialDialog(this).show {
            datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                mBinding.tvTime.text = TimeUtils.date2String(date.time, "yyyy-MM-dd")
            }
        }
    }

    override fun showPriorityDialog() {
        val dialog = BottomSheetDialog(this)

        val view = View.inflate(this, R.layout.dialog_priority, null)
        dialog.setContentView(view)

        val rvPriority: RecyclerView = view.findViewById(R.id.rv_priority)
        val manager = LinearLayoutManager(this)
        rvPriority.layoutManager = manager

        val adapter = PriorityAdapter(mTitles)
        rvPriority.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            mBinding.tvPriority.text = mTitles[position]
            mPriority = position
            mBinding.colorView.setViewColor(SettingManager.getColorByType(mPriority))
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun preSubmit() {
        val title = mBinding.etTitle.text.toString().trim()
        val content = mBinding.etContent.text.toString().trim()
        val time = mBinding.tvTime.text.toString()

        if (StringUtils.isEmpty(title)) {
            ToastUtils.showShort("请输入标题")
            return
        }
        if (StringUtils.isEmpty(content)) {
            ToastUtils.showShort("请输入内容")
            return
        }
        if (StringUtils.isEmpty(time)) {
            ToastUtils.showShort("请选择预定完成时间")
            return
        }

        if (mFrom == 0) {
            getPresenter().submitTodo(title, content, time, 0, mPriority)
        } else {
            getPresenter().editTodo(title, content, time, 0, mPriority, mId)
        }

    }

    override fun setResult() {
        setResult(1000)
        finish()
    }

}