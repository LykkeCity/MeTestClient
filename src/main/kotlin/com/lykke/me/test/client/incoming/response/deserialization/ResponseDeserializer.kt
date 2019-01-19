package com.lykke.me.test.client.incoming.response.deserialization

import com.lykke.me.test.client.incoming.response.Response

interface ResponseDeserializer<T> {
    fun deserialize(response: T): Response
}