package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.CashInOutMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class CashInOutMessageProtoSerializer : MessageProtoSerializer {

    override fun serialize(message: Message): ProtoMessageWrapper {
        message as CashInOutMessage
        val builder = ProtocolMessages.CashInOutOperation.newBuilder()
                .setId(message.requestId)
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetId(message.assetId)
                .setVolume(message.volume.toDouble())
        message.messageId?.let {
            builder.messageId = it
        }
        message.fees.forEach {
            builder.addFees(ProtoUtils.createFeeBuilder(it))
        }
        return ProtoMessageWrapper(builder.build(), message)
    }


}