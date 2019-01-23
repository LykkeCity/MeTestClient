package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date

class CashInOutMessage(val clientId: String,
                       val assetId: String,
                       val volume: BigDecimal,
                       val fees: Collection<SimpleFeeInstruction>,
                       val requestId: String,
                       val messageId: String?,
                       val date: Date) : Message {

    override fun getType() = MessageType.CASH_IN_OUT_OPERATION

    override fun getId() = messageId ?: requestId
}