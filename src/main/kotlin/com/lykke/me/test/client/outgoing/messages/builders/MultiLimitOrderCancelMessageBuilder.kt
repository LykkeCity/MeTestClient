package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderCancelMessage
import java.util.Date
import java.util.UUID

class MultiLimitOrderCancelMessageBuilder : MessageBuilder<MultiLimitOrderCancelMessage> {

    var clientId: String? = null
    var assetPairId: String? = null
    var isBuy: Boolean? = null
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): MultiLimitOrderCancelMessage {
        return MultiLimitOrderCancelMessage(clientId!!,
                assetPairId!!,
                isBuy!!,
                requestId,
                messageId,
                date)
    }
}