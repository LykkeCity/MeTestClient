package com.lykke.me.test.client.incoming.response.deserialization

interface ResponseDeserializerFactory<T> {
    fun createDeserializer(): ResponseDeserializer<T>
}