package com.lykke.me.test.client

import com.lykke.me.test.client.outgoing.messages.Message

interface MeBlockingClient: MeClient {
    fun sendMessageSync(message: Message)
}