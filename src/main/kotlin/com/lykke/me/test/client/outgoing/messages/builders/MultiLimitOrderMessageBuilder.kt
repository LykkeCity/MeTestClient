package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.LimitOrderType
import com.lykke.me.test.client.outgoing.messages.common.OrderCancelMode
import com.lykke.me.test.client.outgoing.messages.common.TimeInForce
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class MultiLimitOrderMessageBuilder : MessageBuilder<MultiLimitOrderMessage> {

    class LimitOrderBuilder {
        var price: BigDecimal? = null
        var volume: BigDecimal? = null
        var fee: FeeInstruction? = null
        var fees: List<FeeInstruction> = emptyList()
        var externalId: String = UUID.randomUUID().toString()
        var oldUid: String? = null
        var timeInForce: TimeInForce? = null
        var expiryTime: Date? = null
        var type: LimitOrderType? = null
        var lowerLimitPrice: BigDecimal? = null
        var lowerPrice: BigDecimal? = null
        var upperLimitPrice: BigDecimal? = null
        var upperPrice: BigDecimal? = null

        fun build(): MultiLimitOrderMessage.LimitOrder {
            return MultiLimitOrderMessage.LimitOrder(price,
                    volume!!,
                    fee,
                    fees,
                    externalId,
                    oldUid,
                    timeInForce,
                    expiryTime,
                    type,
                    lowerLimitPrice,
                    lowerPrice,
                    upperLimitPrice,
                    upperPrice)
        }
    }

    var clientId: String? = null
    var assetPairId: String? = null
    var orders: List<LimitOrderBuilder> = emptyList()
    var cancelAllPreviousLimitOrders: Boolean? = true
    var cancelMode: OrderCancelMode? = null
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): MultiLimitOrderMessage {
        return MultiLimitOrderMessage(clientId!!,
                assetPairId!!,
                orders.map { it.build() },
                cancelAllPreviousLimitOrders,
                cancelMode,
                requestId,
                messageId,
                date)
    }
}