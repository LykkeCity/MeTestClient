package com.lykke.me.test.client

interface MeListener<T> {
    fun subscribe(subscriber: MeSubscriber<T>)
    fun unsubscribe(subscriber: MeSubscriber<T>)
}