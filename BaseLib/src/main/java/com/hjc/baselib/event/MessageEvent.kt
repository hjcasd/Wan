package com.hjc.baselib.event

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:55
 * @Description: 封装用于EventBus传递消息类
 */
class MessageEvent<T> {
    var code: Int
    var data: T? = null
        private set

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    fun setData(data: T) {
        this.data = data
    }
}