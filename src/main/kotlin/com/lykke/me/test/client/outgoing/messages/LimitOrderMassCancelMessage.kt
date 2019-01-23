package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType

class LimitOrderMassCancelMessage(val clientId: String,
                                  val assetPairId: String?,
                                  val isBuy: Boolean?,
                                  val requestId: String,
                                  val messageId: String?) : Message {

    override fun getType() = MessageType.LIMIT_ORDER_MASS_CANCEL

    override fun getId() = messageId ?: requestId
}