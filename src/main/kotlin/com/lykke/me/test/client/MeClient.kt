package com.lykke.me.test.client

import com.lykke.me.test.client.outgoing.messages.Message

interface MeClient {
    fun sendMessage(message: Message)
    fun sendMessages(messages: List<Message>)
}