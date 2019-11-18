package com.hjc.wan.model

import java.io.Serializable

data class IntegralBean(
    var coinCount: Int,
    var rank: Int,
    var userId: Int,
    var username: String
) : Serializable