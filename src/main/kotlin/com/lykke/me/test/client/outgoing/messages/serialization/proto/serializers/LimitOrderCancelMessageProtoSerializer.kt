package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.LimitOrderCancelMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class LimitOrderCancelMessageProtoSerializer : MessageProtoSerializer {
    
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as LimitOrderCancelMessage
        val builder = ProtocolMessages.LimitOrderCancel.newBuilder()
                .setUid(message.requestId)
                .addAllLimitOrderId(message.orderIds)
        message.messageId?.let {
            builder.messageId = it
        }
        return ProtoMessageWrapper(builder.build(), message.getType())
    }
}