package com.lykke.me.test.client.outgoing.messages.builders

import com.lykke.me.test.client.outgoing.messages.CashTransferMessage
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

class CashTransferMessageBuilder : MessageBuilder<CashTransferMessage> {

    var fromClientId: String? = null
    var toClientId: String? = null
    var assetId: String? = null
    var volume: BigDecimal? = null
    var fee: SimpleFeeInstruction? = null
    var overdraftLimit: BigDecimal? = null
    var fees: List<SimpleFeeInstruction> = emptyList()
    var requestId: String = UUID.randomUUID().toString()
    var messageId: String? = null
    var date: Date = Date()

    override fun build(): CashTransferMessage {
        return CashTransferMessage(fromClientId!!,
                toClientId!!,
                assetId!!,
                volume!!,
                fee,
                overdraftLimit,
                fees,
                requestId,
                messageId,
                date)
    }
}