package com.hjc.wan.model

import java.io.Serializable

data class NavigationBean(
    var articles: MutableList<ArticleBean>,
    var cid: Int,
    var name: String
) : Serializable