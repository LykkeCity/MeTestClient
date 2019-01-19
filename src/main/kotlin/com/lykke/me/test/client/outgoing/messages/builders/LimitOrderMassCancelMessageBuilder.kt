package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.LimitOrderMassCancelMessage
import java.util.UUID

class LimitOrderMassCancelMessageBuilder: MessageBuilder<LimitOrderMassCancelMessage> {

    var clientId: String? = null
    var assetPairId: String? = null
    var isBuy: Boolean? = null
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null

    override fun build(): LimitOrderMassCancelMessage {
        return LimitOrderMassCancelMessage(clientId!!,
                assetPairId,
                isBuy,
                requestId,
                messageId)
    }

}