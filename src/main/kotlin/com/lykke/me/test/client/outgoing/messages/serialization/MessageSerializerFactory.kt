package com.lykke.me.test.client.outgoing.messages.serialization

interface MessageSerializerFactory<T> {
    fun createSerializer(): MessageSerializer<T>
}