package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import java.math.BigDecimal
import java.util.Date

class ReservedCashInOutMessage(val clientId: String,
                               val assetId: String,
                               val reservedVolume: BigDecimal,
                               val requestId: String,
                               val messageId: String?,
                               val date: Date) : Message {

    override fun getType() = MessageType.RESERVED_CASH_IN_OUT_OPERATION

    override fun getId() = messageId ?: requestId
}