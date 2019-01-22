package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.LimitOrderType
import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.common.TimeInForce
import java.math.BigDecimal
import java.util.Date

class LimitOrderMessage(val clientId: String,
                        val assetPairId: String,
                        val volume: BigDecimal,
                        val price: BigDecimal?,
                        val fee: FeeInstruction?,
                        val fees: List<FeeInstruction>,
                        val externalId: String,
                        val type: LimitOrderType?,
                        val lowerLimitPrice: BigDecimal?,
                        val lowerPrice: BigDecimal?,
                        val upperLimitPrice: BigDecimal?,
                        val upperPrice: BigDecimal?,
                        val messageId: String?,
                        val cancelAllPreviousLimitOrders: Boolean?,
                        val timeInForce: TimeInForce?,
                        val expiryTime: Date?,
                        val date: Date) : Message {

    override fun getType() = MessageType.LIMIT_ORDER

    override fun getId() = messageId ?: externalId
}