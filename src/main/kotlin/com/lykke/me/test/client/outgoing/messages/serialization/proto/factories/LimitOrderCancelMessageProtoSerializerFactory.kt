package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.LimitOrderCancelMessageProtoSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper

class LimitOrderCancelMessageProtoSerializerFactory: MessageProtoSerializerFactory {
    override fun createSerializer(): MessageSerializer<ProtoMessageWrapper> {
        return LimitOrderCancelMessageProtoSerializer()
    }

}