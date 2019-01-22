package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.LimitOrderMassCancelMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class LimitOrderMassCancelMessageProtoSerializer : MessageProtoSerializer {
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as LimitOrderMassCancelMessage
        val builder = ProtocolMessages.LimitOrderMassCancel.newBuilder()
                .setUid(message.requestId)
                .setClientId(message.clientId)
        message.assetPairId?.let {
            builder.assetPairId = it
        }
        message.isBuy?.let {
            builder.isBuy = it
        }
        message.messageId?.let {
            builder.messageId = it
        }
        return ProtoMessageWrapper(builder.build(), message)
    }

}