package com.lykke.me.test.client.outgoing.messages.serialization

import com.lykke.me.test.client.outgoing.messages.Message

interface MessageSerializer<T> {
    fun serialize(message: Message): T
}