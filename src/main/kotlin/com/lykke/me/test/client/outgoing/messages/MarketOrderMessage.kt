package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date

class MarketOrderMessage(val clientId: String,
                         val assetPairId: String,
                         val volume: BigDecimal,
                         val straight: Boolean,
                         val reservedLimitVolume: BigDecimal?,
                         val fee: SimpleFeeInstruction?,
                         val fees: List<SimpleFeeInstruction>,
                         val externalId: String,
                         val messageId: String?,
                         val date: Date) : Message {

    override fun getType() = MessageType.MARKET_ORDER
}