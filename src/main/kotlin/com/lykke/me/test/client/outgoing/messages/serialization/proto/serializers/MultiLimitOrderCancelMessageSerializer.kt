package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderCancelMessage
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages
import java.util.UUID

class MultiLimitOrderCancelMessageSerializer : MessageProtoSerializer {
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as MultiLimitOrderCancelMessage
        val builder = ProtocolMessages.MultiLimitOrderCancel.newBuilder()
                .setUid(UUID.randomUUID().toString())
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetPairId(message.assetPairId)
                .setIsBuy(message.isBuy)
        message.messageId?.let {
            builder.messageId = it
        }
        return ProtoMessageWrapper(builder.build(), message)
    }
}