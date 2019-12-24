package com.hjc.wan.ui.todo.child

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjc.wan.R
import com.hjc.wan.base.BaseMvpActivity
import com.hjc.wan.constant.RoutePath
import com.hjc.wan.ui.todo.adapter.PriorityAdapter
import com.hjc.wan.ui.todo.contract.AddTodoContract
import com.hjc.wan.ui.todo.presenter.AddTodoPresenter
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

    private var mPriority : Int? = 0


    override fun createPresenter(): AddTodoPresenter {
        return AddTodoPresenter()
    }

    override fun createView(): AddTodoContract.View {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_todo
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        bundle?.let {
            val from = it.getInt("from")

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

    @SuppressLint("SetTextI18n")
    override fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, month, day ->
                tvTime.text = "${year}-${month + 1}-${day}"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    override fun showPriorityDialog() {
        val dialog = BottomSheetDialog(this)

        val view = View.inflate(this, R.layout.dialog_priority, null)
        dialog.setContentView(view)

        val rvPriority: RecyclerView = view.findViewById(R.id.rv_priority)
        val manager = LinearLayoutManager(this)
        rvPriority.layoutManager = manager

        val titles = mutableListOf("重要且紧急", "重要但不紧急", "紧急但不重要", "不重要不紧急")
        val adapter = PriorityAdapter(titles)
        rvPriority.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            tvPriority.text = titles[position]
            mPriority = position
            when (position) {
                0 -> {
                    ivCircle.setImageResource(R.drawable.shape_circle_red)
                }
                1 -> {
                    ivCircle.setImageResource(R.drawable.shape_circle_yelllow)
                }
                2 -> {
                    ivCircle.setImageResource(R.drawable.shape_circle_blue)
                }
                3 -> {
                    ivCircle.setImageResource(R.drawable.shape_circle_green)
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun preSubmit() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()
        val time = tvTime.text.toString()

        if (StringUtils.isEmpty(title)){
            ToastUtils.showShort("请输入标题")
            return
        }
        if (StringUtils.isEmpty(content)){
            ToastUtils.showShort("请输入内容")
            return
        }
        if (StringUtils.isEmpty(time)){
            ToastUtils.showShort("请选择预定完成时间")
            return
        }

        getPresenter()?.submitTodo(title, content, time, 0, mPriority!!)
    }

}