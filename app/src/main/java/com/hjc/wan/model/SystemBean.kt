package com.hjc.wan.model

import java.io.Serializable

data class SystemBean(
    var children: MutableList<ClassifyBean>,
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int
) : Serializable