package com.lykke.me.test.client.utils

import com.lykke.me.test.client.outgoing.messages.Message

fun generateMessages(count: Int, strategy: (Int) -> Message): List<Message> {
    return IntRange(0, count).map { strategy.invoke(it) }
}