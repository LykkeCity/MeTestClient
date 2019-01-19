package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.ReservedCashInOutMessage
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class ReservedCashInOutMessageProtoSerializer: MessageProtoSerializer {
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as ReservedCashInOutMessage
        val builder = ProtocolMessages.ReservedCashInOutOperation.newBuilder()
                .setId(message.requestId)
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetId(message.assetId)
                .setReservedVolume(message.reservedVolume.toDouble())
        message.messageId?.let {
            builder.messageId = it
        }
        return ProtoMessageWrapper(builder.build(), message.getType())
    }
}