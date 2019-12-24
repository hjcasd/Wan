package com.hjc.wan.model

import com.blankj.utilcode.util.TimeUtils
import java.io.Serializable

data class TodoBean(
    var completeDate: Long,
    var completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    var id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    var userId: Int
) : Serializable {

    fun isDone(): Boolean {
        //判断是否已完成或者已过期
        return if (status == 1) {
            true
        } else {
            date < TimeUtils.getNowMills()
        }
    }
}
