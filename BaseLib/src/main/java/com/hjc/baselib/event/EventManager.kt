package com.hjc.baselib.event

import org.greenrobot.eventbus.EventBus

/**
 * @Author: HJC
 * @Date: 2019/1/4 14:57
 * @Description: EventBus管理类
 */
object EventManager {

    fun register(subscriber: Any?) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }

    fun unregister(subscriber: Any?) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    fun sendEvent(event: MessageEvent<*>?) {
        EventBus.getDefault().post(event)
    }

    fun sendStickyEvent(event: MessageEvent<*>?) {
        EventBus.getDefault().postSticky(event)
    }
}