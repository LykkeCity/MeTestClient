package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.MarketOrderMessage
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class MarketOrderMessageBuilder: MessageBuilder<MarketOrderMessage> {

    var clientId: String? = null
    var assetPairId: String? = null
    var volume: BigDecimal? = null
    var straight: Boolean = true
    var reservedLimitVolume: BigDecimal? = null
    var fee: SimpleFeeInstruction? = null
    var fees: List<SimpleFeeInstruction> = emptyList()
    var externalId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): MarketOrderMessage {
        return MarketOrderMessage(clientId!!,
                assetPairId!!,
                volume!!,
                straight,
                reservedLimitVolume,
                fee,
                fees,
                externalId,
                messageId,
                date)
    }
}