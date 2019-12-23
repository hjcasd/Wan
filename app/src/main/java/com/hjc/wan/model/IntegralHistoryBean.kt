package com.hjc.wan.model

import java.io.Serializable

data class IntegralHistoryBean(
    var coinCount: Int,
    var date: Long,
    var desc: String,
    var id: Int,
    var type: Int,
    var reason: String,
    var userId: Int,
    var userName: String
) : Serializable