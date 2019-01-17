package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.CashInOutMessage
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class CashInOutMessageBuilder : MessageBuilder<CashInOutMessage> {

    var clientId: String? = null
    var assetId: String? = null
    var volume: BigDecimal? = null
    var fees: Collection<SimpleFeeInstruction> = emptyList()
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): CashInOutMessage {
        return CashInOutMessage(clientId!!,
                assetId!!,
                volume!!,
                fees,
                requestId,
                messageId,
                date)
    }
}