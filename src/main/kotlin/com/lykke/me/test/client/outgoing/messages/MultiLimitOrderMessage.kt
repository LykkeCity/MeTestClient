package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.LimitOrderType
import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.common.OrderCancelMode
import com.lykke.me.test.client.outgoing.messages.common.TimeInForce
import java.math.BigDecimal
import java.util.Date

class MultiLimitOrderMessage(val clientId: String,
                             val assetPairId: String,
                             val orders: List<LimitOrder>,
                             val cancelAllPreviousLimitOrders: Boolean?,
                             val cancelMode: OrderCancelMode?,
                             val requestId: String,
                             val messageId: String?,
                             val date: Date): Message {
    override fun getType() = MessageType.MULTI_LIMIT_ORDER

    override fun getId() = messageId ?: requestId

    class LimitOrder(val price: BigDecimal?,
                     val volume: BigDecimal,
                     val fee: FeeInstruction?,
                     val fees: List<FeeInstruction>,
                     val externalId: String,
                     val oldUid: String?,
                     val timeInForce: TimeInForce?,
                     val expiryTime: Date?,
                     val type: LimitOrderType?,
                     val lowerLimitPrice: BigDecimal?,
                     val lowerPrice: BigDecimal?,
                     val upperLimitPrice: BigDecimal?,
                     val upperPrice: BigDecimal?)
}