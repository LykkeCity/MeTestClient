package com.lykke.me.test.client

interface MeResponseListener {
    fun subscribe(subscriber: MeResponseSubscriber)
    fun unsubscribe(subscriber: MeResponseSubscriber)
}