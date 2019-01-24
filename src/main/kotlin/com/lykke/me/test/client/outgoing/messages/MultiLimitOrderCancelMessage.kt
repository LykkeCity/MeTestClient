package com.lykke.me.test.client.outgoing.messages

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import java.util.Date

class MultiLimitOrderCancelMessage(val clientId: String,
                                   val assetPairId: String,
                                   val isBuy: Boolean,
                                   val requestId: String,
                                   val messageId: String?,
                                   val date: Date) : Message {

    override fun getType() = MessageType.MULTI_LIMIT_ORDER_CANCEL

    override fun getId() = messageId ?: requestId
}