package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.LimitOrderCancelMessage
import java.util.UUID

class LimitOrderCancelMessageBuilder: MessageBuilder<LimitOrderCancelMessage> {

    var orderIds: Collection<String> = emptyList()
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null

    override fun build(): LimitOrderCancelMessage {
        return LimitOrderCancelMessage(orderIds, requestId, messageId)
    }
}