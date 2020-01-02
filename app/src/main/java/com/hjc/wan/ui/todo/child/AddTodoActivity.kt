package com.hjc.wan.ui.todo.child

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.model.TodoBean
import com.hjc.wan.ui.todo.adapter.PriorityAdapter
import com.hjc.wan.ui.todo.contract.AddTodoContract
import com.hjc.wan.ui.todo.presenter.AddTodoPresenter
import com.hjc.wan.utils.helper.SettingManager
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.util.*


/**
 * @Author: HJC
 * @Date: 2019/12/24 14:06
 * @Description: 添加待办清单页面
 */
@Route(path = RoutePath.URL_ADD_TO_DO)
class AddTodoActivity : BaseMvpActivity<AddTodoContract.View, AddTodoPresenter>(),
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

    override fun getLayoutId(): Int {
        return R.layout.activity_add_todo
    }

    override fun initView() {
        super.initView()

        SettingManager.getThemeColor().let {
            titleBar.setBackgroundColor(it)
            val drawable = btnSubmit.background as GradientDrawable
            drawable.setColor(it)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        bundle?.let {
            mFrom = it.getInt("from")
            if (mFrom == 1) {
                titleBar.setTitle("编辑待办事项")

                val bean = it.getSerializable("bean") as TodoBean
                mId = bean.id
                etTitle.setText(bean.title)
                etContent.setText(bean.content)
                tvTime.text = bean.dateStr

                mPriority = bean.priority
                tvPriority.text = mTitles[mPriority]

                colorView.setViewColor(SettingManager.getColorByType(mPriority))
            } else {
                colorView.setViewColor(SettingManager.getColorByType(0))
                titleBar.setTitle("添加待办事项")
            }
        }
    }

    override fun addListeners() {
        super.addListeners()

        tvTime.setOnClickListener(this)
        llPriority.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        titleBar.setOnViewLeftClickListener { finish() }
    }

    override fun onSingleClick(v: View?) {
        super.onSingleClick(v)
        when (v?.id) {
            R.id.tvTime -> {
                showDatePicker()
            }

            R.id.llPriority -> {
                showPriorityDialog()
            }

            R.id.btnSubmit -> {
                preSubmit()
            }
        }
    }

    override fun showDatePicker() {
        MaterialDialog(this).show {
            datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                this@AddTodoActivity.tvTime.text = TimeUtils.date2String(date.time, "yyyy-MM-dd")
            }
        }
    }

    override fun showPriorityDialog() {
        val dialog = BottomSheetDialog(this)

        val view = View.inflate(this, R.layout.dialog_priority, null)
        dialog.setContentView(view)

        val rvPriority: androidx.recyclerview.widget.RecyclerView = view.findViewById(R.id.rv_priority)
        val manager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvPriority.layoutManager = manager

        val adapter = PriorityAdapter(mTitles)
        rvPriority.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            tvPriority.text = mTitles[position]
            mPriority = position
            colorView.setViewColor(SettingManager.getColorByType(mPriority))
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun preSubmit() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()
        val time = tvTime.text.toString()

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
            getPresenter()?.submitTodo(title, content, time, 0, mPriority)
        } else {
            getPresenter()?.editTodo(title, content, time, 0, mPriority, mId)
        }

    }

}