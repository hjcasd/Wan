package com.hjc.wan.model

import java.io.Serializable

data class SearchBean(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
) : Serializable