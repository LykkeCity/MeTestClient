package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.Message

interface MessageBuilder<T: Message> {
    fun build(): T
}