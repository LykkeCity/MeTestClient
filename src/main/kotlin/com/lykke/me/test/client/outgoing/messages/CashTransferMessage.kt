package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date

class CashTransferMessage(val fromClientId: String,
                          val toClientId: String,
                          val assetId: String,
                          val volume: BigDecimal,
                          val fee: SimpleFeeInstruction?,
                          val overdraftLimit: BigDecimal?,
                          val fees: List<SimpleFeeInstruction>,
                          val requestId: String,
                          val messageId: String?,
                          val date: Date) : Message {

    override fun getType() = MessageType.CASH_TRANSFER_OPERATION

    override fun getId() = messageId ?: requestId
}