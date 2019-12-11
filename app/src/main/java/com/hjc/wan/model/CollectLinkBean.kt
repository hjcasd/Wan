package com.hjc.wan.model

import java.io.Serializable

data class CollectLinkBean(
    var icon: String,
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var userId: Int,
    var visible: Int
) : Serializable