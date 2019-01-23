package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType

class LimitOrderCancelMessage(val orderIds: Collection<String>,
                              val requestId: String,
                              val messageId: String?) : Message {

    override fun getType() = MessageType.LIMIT_ORDER_CANCEL

    override fun getId() = messageId ?: requestId
}