package com.lykke.me.test.client

interface MeSubscriber<T> {
    fun notify(message: T)
}