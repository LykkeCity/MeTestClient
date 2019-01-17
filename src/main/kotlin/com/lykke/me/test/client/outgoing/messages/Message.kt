package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType

interface Message {
    fun getType(): MessageType
}