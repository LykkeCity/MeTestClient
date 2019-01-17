package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.ReservedCashInOutMessage
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class ReservedCashInOutMessageBuilder : MessageBuilder<ReservedCashInOutMessage> {

    var clientId: String? = null
    var assetId: String? = null
    var reservedVolume: BigDecimal? = null
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): ReservedCashInOutMessage {
        return ReservedCashInOutMessage(clientId!!,
                assetId!!,
                reservedVolume!!,
                requestId,
                messageId,
                date)
    }
}