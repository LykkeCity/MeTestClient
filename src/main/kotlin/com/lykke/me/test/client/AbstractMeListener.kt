package com.lykke.me.test.client

import java.util.concurrent.CopyOnWriteArraySet

abstract class AbstractMeListener<T>: MeListener<T> {

    private val subscribers = CopyOnWriteArraySet<MeSubscriber<T>>()

    override fun subscribe(subscriber: MeSubscriber<T>) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: MeSubscriber<T>) {
        subscribers.remove(subscriber)
    }

    protected fun notifySubscribers(message: T) {
        subscribers.forEach {
            it.notify(message)
        }
    }
}