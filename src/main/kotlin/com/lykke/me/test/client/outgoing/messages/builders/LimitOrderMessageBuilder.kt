package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.LimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.LimitOrderType
import com.lykke.me.test.client.outgoing.messages.common.TimeInForce
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class LimitOrderMessageBuilder : MessageBuilder<LimitOrderMessage> {

    var clientId: String? = null
    var assetPairId: String? = null
    var volume: BigDecimal? = null
    var price: BigDecimal? = null
    var fee: FeeInstruction? = null
    var fees: List<FeeInstruction> = emptyList()
    var externalId: String = UUID.randomUUID().toString()
    var type: LimitOrderType? = null
    var lowerLimitPrice: BigDecimal? = null
    var lowerPrice: BigDecimal? = null
    var upperLimitPrice: BigDecimal? = null
    var upperPrice: BigDecimal? = null
    var messageId: String? = null
    var cancelAllPreviousLimitOrders: Boolean? = null
    var timeInForce: TimeInForce? = null
    var expiryTime: Date? = null
    var date: Date = Date()

    override fun build(): LimitOrderMessage {
        return LimitOrderMessage(clientId!!,
                assetPairId!!,
                volume!!,
                price,
                fee,
                fees,
                externalId,
                type,
                lowerLimitPrice,
                lowerPrice,
                upperLimitPrice,
                upperPrice,
                messageId,
                cancelAllPreviousLimitOrders,
                timeInForce,
                expiryTime,
                date)
    }
}